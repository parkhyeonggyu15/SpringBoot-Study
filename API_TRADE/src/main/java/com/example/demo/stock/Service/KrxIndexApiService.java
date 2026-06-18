package com.example.demo.stock.Service;

import com.example.demo.stock.Entity.KospiIndexPrice;
import com.example.demo.stock.Repository.KospiIndexPriceRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KrxIndexApiService {

    private final KospiIndexPriceRepository kospiIndexPriceRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${krx.api.key}")
    private String krxApiKey;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * KRX 정보데이터시스템 - KOSPI 시리즈 일별시세정보를 가져와 DB에 저장합니다.
     * @param basDd 조회 기준일자 (YYYYMMDD), 영업일에만 데이터가 존재합니다.
     */
    @Transactional
    public List<KospiIndexPrice> fetchAndSaveKospiSeries(String basDd) {
        URI uri = UriComponentsBuilder.fromUriString("https://data-dbg.krx.co.kr/svc/apis/idx/kospi_dd_trd")
                .queryParam("basDd", basDd)
                .build(true)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("AUTH_KEY", krxApiKey);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON, MediaType.ALL));

        // 💡 응답을 바로 Map/JSON으로 받지 않고 String으로 먼저 받아, 인증 실패 등으로 HTML이 와도 원문을 확인할 수 있게 합니다.
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        String responseBody = response.getBody();

        log.info("[KRX KOSPI 시리즈 API 응답] status={}, body={}", response.getStatusCode(), responseBody);

        JsonNode root;
        try {
            root = objectMapper.readTree(responseBody);
        } catch (Exception e) {
            throw new IllegalStateException("KRX API 응답이 JSON 형식이 아닙니다 (인증키 또는 요청 URL을 확인하세요). 응답 원문: "
                    + (responseBody != null ? responseBody.substring(0, Math.min(300, responseBody.length())) : "없음"));
        }

        JsonNode outBlock = root.get("OutBlock_1");
        if (outBlock == null || !outBlock.isArray() || outBlock.isEmpty()) {
            throw new IllegalStateException("해당 날짜(" + basDd + ")는 영업일이 아니거나 KOSPI 시리즈 시세 정보가 없습니다. 응답 원문: " + responseBody);
        }

        List<KospiIndexPrice> kospiIndexPrices = new ArrayList<>();
        for (JsonNode item : outBlock) {
            kospiIndexPrices.add(toEntity(item));
        }

        return kospiIndexPriceRepository.saveAll(kospiIndexPrices);
    }

    // 오늘 기준 영업일 날짜(YYYYMMDD)로 KOSPI 시리즈 시세를 가져와 저장합니다.
    @Transactional
    public List<KospiIndexPrice> fetchAndSaveKospiSeriesForToday() {
        return fetchAndSaveKospiSeries(LocalDate.now().format(DATE_FORMAT));
    }

    private KospiIndexPrice toEntity(JsonNode item) {
        return KospiIndexPrice.builder()
                .basDd(textValue(item, "BAS_DD"))
                .idxNm(textValue(item, "IDX_NM"))
                .clsprcIdx(parseDouble(textValue(item, "CLSPRC_IDX")))
                .cmpprevddIdx(parseDouble(textValue(item, "CMPPREVDD_IDX")))
                .flucRt(parseDouble(textValue(item, "FLUC_RT")))
                .opnprcIdx(parseDouble(textValue(item, "OPNPRC_IDX")))
                .hgprcIdx(parseDouble(textValue(item, "HGPRC_IDX")))
                .lwprcIdx(parseDouble(textValue(item, "LWPRC_IDX")))
                .accTrdvol(parseLong(textValue(item, "ACC_TRDVOL")))
                .accTrdval(parseLong(textValue(item, "ACC_TRDVAL")))
                .mktcap(parseLong(textValue(item, "MKTCAP")))
                .build();
    }

    private String textValue(JsonNode item, String field) {
        JsonNode node = item.get(field);
        return (node == null || node.isNull()) ? null : node.asText();
    }

    // KRX 응답값(콤마 포함 가능)을 Double로 변환합니다. 값이 없으면 null을 반환합니다.
    private Double parseDouble(String raw) {
        if (raw == null) {
            return null;
        }
        String text = raw.replace(",", "").trim();
        return text.isBlank() ? null : Double.parseDouble(text);
    }

    // KRX 응답값(콤마 포함 가능)을 Long으로 변환합니다. 값이 없으면 null을 반환합니다.
    private Long parseLong(String raw) {
        if (raw == null) {
            return null;
        }
        String text = raw.replace(",", "").trim();
        return text.isBlank() ? null : Long.parseLong(text);
    }
}
