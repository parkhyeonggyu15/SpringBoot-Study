package com.example.demo.stock.Service;

import com.example.demo.stock.Dtos.ExchangeRateChange;
import com.example.demo.stock.Entity.ExchangeRate;
import com.example.demo.stock.Repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeRateApiService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${koreaexim.api.key}")
    private String koreaeximApiKey;

    // 한국수출입은행 환율 API의 cur_unit 코드 (한국은 원화 기준이라 API 응답에 자체 항목이 없음)
    private static final String JAPAN_CUR_UNIT = "JPY(100)";
    private static final String USA_CUR_UNIT = "USD";
    private static final String CHINA_CUR_UNIT = "CNH";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int MAX_PREVIOUS_DAY_LOOKUP = 10; // 연휴가 길어도 이전 영업일을 찾을 수 있도록 최대 10일 전까지 탐색

    /**
     * 한국수출입은행 환율 API(exchangeJSON)에서 한국/일본/미국/중국 환율 정보를 가져와 DB에 저장합니다.
     * @param searchDate 조회 기준일자 (YYYYMMDD), 평일(영업일) 기준으로만 데이터가 존재합니다.
     */
    @Transactional
    public List<ExchangeRate> fetchAndSaveExchangeRates(String searchDate) {
        return exchangeRateRepository.saveAll(callExchangeApi(searchDate));
    }

    // 오늘 기준 영업일 날짜(YYYYMMDD)로 환율 정보를 가져와 저장합니다.
    @Transactional
    public List<ExchangeRate> fetchAndSaveExchangeRatesForToday() {
        return fetchAndSaveExchangeRates(LocalDate.now().format(DATE_FORMAT));
    }

    /**
     * 기준일자 환율을 전 영업일 환율과 비교하여 등락률을 계산합니다.
     * 기준일자 데이터는 DB에 저장하고, 비교용 전 영업일 데이터는 저장하지 않습니다.
     */
    @Transactional
    public List<ExchangeRateChange> getExchangeRateChanges(String searchDate) {
        List<ExchangeRate> currentRates = fetchAndSaveExchangeRates(searchDate);
        List<ExchangeRate> previousRates = findPreviousBusinessDayRates(LocalDate.parse(searchDate, DATE_FORMAT));

        Map<String, ExchangeRate> previousByCountry = new LinkedHashMap<>();
        for (ExchangeRate rate : previousRates) {
            previousByCountry.put(rate.getCountry(), rate);
        }

        List<ExchangeRateChange> changes = new java.util.ArrayList<>();
        for (ExchangeRate current : currentRates) {
            ExchangeRate previous = previousByCountry.get(current.getCountry());
            double changeAmount = current.getDealBasR() - previous.getDealBasR();
            double changeRate = (changeAmount / previous.getDealBasR()) * 100;
            changes.add(new ExchangeRateChange(current, previous, changeAmount, changeRate));
        }
        return changes;
    }

    // searchDate 이전 영업일을 하루씩 거슬러 올라가며 환율 정보를 찾습니다 (주말/공휴일은 API가 빈 응답을 주므로 건너뜀).
    private List<ExchangeRate> findPreviousBusinessDayRates(LocalDate searchDate) {
        LocalDate candidate = searchDate.minusDays(1);
        for (int attempt = 0; attempt < MAX_PREVIOUS_DAY_LOOKUP; attempt++) {
            try {
                return callExchangeApi(candidate.format(DATE_FORMAT));
            } catch (IllegalStateException e) {
                candidate = candidate.minusDays(1);
            }
        }
        throw new IllegalStateException("전 영업일 환율 정보를 찾을 수 없습니다 (최대 " + MAX_PREVIOUS_DAY_LOOKUP + "일 탐색).");
    }

    // koreaexim 환율 API를 호출해 한국/일본/미국/중국 환율을 조립합니다 (DB 저장은 호출자 책임).
    private List<ExchangeRate> callExchangeApi(String searchDate) {
        URI uri = UriComponentsBuilder.fromUriString("https://oapi.koreaexim.go.kr/site/program/financial/exchangeJSON")
                .queryParam("authkey", koreaeximApiKey)
                .queryParam("searchdate", searchDate)
                .queryParam("data", "AP01")
                .build(true)
                .toUri();

        List<Map<String, Object>> response = restTemplate.getForObject(uri, List.class);

        if (response == null || response.isEmpty()) {
            throw new IllegalStateException("해당 날짜(" + searchDate + ")는 주말/공휴일이거나 환율 정보가 없습니다.");
        }

        Object firstResult = response.get(0).get("result");
        if (firstResult != null && !"1".equals(String.valueOf(firstResult))) {
            throw new IllegalStateException("환율 API 호출 실패 (result 코드: " + firstResult + ")");
        }

        // cur_unit 기준으로 빠르게 조회하기 위한 맵 구성
        Map<String, Map<String, Object>> itemsByCurUnit = new LinkedHashMap<>();
        for (Map<String, Object> item : response) {
            Object curUnit = item.get("cur_unit");
            if (curUnit != null) {
                itemsByCurUnit.put(String.valueOf(curUnit), item);
            }
        }

        List<ExchangeRate> result = new java.util.ArrayList<>();
        result.add(buildKoreaExchangeRate(searchDate)); // 한국(KRW)은 기준 화폐이므로 API 응답에 없어 직접 생성
        result.add(buildExchangeRate("JAPAN", itemsByCurUnit.get(JAPAN_CUR_UNIT), searchDate));
        result.add(buildExchangeRate("USA", itemsByCurUnit.get(USA_CUR_UNIT), searchDate));
        result.add(buildExchangeRate("CHINA", itemsByCurUnit.get(CHINA_CUR_UNIT), searchDate));

        return result;
    }

    private ExchangeRate buildKoreaExchangeRate(String searchDate) {
        return ExchangeRate.builder()
                .country("KOREA")
                .curUnit("KRW")
                .curNm("한국 원")
                .baseDate(searchDate)
                .ttb(1.0)
                .tts(1.0)
                .dealBasR(1.0)
                .build();
    }

    private ExchangeRate buildExchangeRate(String country, Map<String, Object> item, String searchDate) {
        if (item == null) {
            throw new IllegalStateException(country + "의 환율 정보를 응답에서 찾을 수 없습니다.");
        }

        return ExchangeRate.builder()
                .country(country)
                .curUnit(String.valueOf(item.get("cur_unit")))
                .curNm(String.valueOf(item.get("cur_nm")))
                .baseDate(searchDate)
                .ttb(parseRate(item.get("ttb")))
                .tts(parseRate(item.get("tts")))
                .dealBasR(parseRate(item.get("deal_bas_r")))
                .build();
    }

    // "1,313.00" 형태의 콤마 포함 환율 문자열을 Double로 변환합니다.
    private Double parseRate(Object raw) {
        if (raw == null) {
            return null;
        }
        String text = String.valueOf(raw).replace(",", "").trim();
        if (text.isBlank()) {
            return null;
        }
        return Double.parseDouble(text);
    }
}
