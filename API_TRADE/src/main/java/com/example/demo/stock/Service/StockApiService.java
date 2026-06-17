package com.example.demo.stock.Service;

import com.example.demo.stock.Entity.CorpCodeMapping;
import com.example.demo.stock.Entity.FinancialStatement;
import com.example.demo.stock.Entity.StockPrice;
import com.example.demo.stock.Repository.CorpCodeMappingRepository;
import com.example.demo.stock.Repository.FinancialStatementRepository;
import com.example.demo.stock.Repository.StockPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate; // 💡 변경: RestTemplate 임포트
import org.springframework.web.util.UriComponentsBuilder;
import java.time.Year;
import java.util.stream.Collectors;
import java.net.URI;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StockApiService {

    private final StockPriceRepository stockPriceRepository;
    private final DartCorpCodeService dartCorpCodeService;
    private final CorpCodeMappingRepository corpCodeMappingRepository;
    private final FinancialStatementRepository financialStatementRepository;

    // 💡 Netty 대신 자바 기본 보안 설정을 100% 따르는 RestTemplate을 선언합니다.
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${PublicDataKey}")
    private String publicDataKey;

    @Value("${OpenDartKey}")
    private String openDartKey;

    // KRX 종목코드에 대응하는 corp_code_mapping row의 시가총액을 갱신합니다. 매핑이 없으면 건너뜁니다.
    private void updateMarketCap(String stockCode, long mrktTotAmt) {
        corpCodeMappingRepository.findByStockCode(stockCode)
                .ifPresent(mapping -> {
                    mapping.updateMrktTotAmt(mrktTotAmt);
                    corpCodeMappingRepository.save(mapping);
                });
    }

    /**
     * 1. 공공데이터포털 - 주식시세정보 가져와서 DB 저장
     */
    @Transactional
    public void fetchAndSaveStockPrice(String stockCode) {
        URI uri = UriComponentsBuilder.fromUriString("http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo")
                .queryParam("serviceKey", publicDataKey)
                .queryParam("resultType", "json")
                .queryParam("likeSrtnCd", stockCode)
                .queryParam("numOfRows", "1")
                .build(true)
                .toUri();

        // 💡 WebClient의 복잡한 체이닝 대신 getForObject로 한 번에 Map으로 받아옵니다.
        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);

        try {
            if (response != null) {
                Map<String, Object> responseBody = (Map<String, Object>) response.get("response");
                Map<String, Object> body = (Map<String, Object>) responseBody.get("body");
                Map<String, Object> items = (Map<String, Object>) body.get("items");
                List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");

                if (itemList != null && !itemList.isEmpty()) {
                    Map<String, Object> item = itemList.get(0);

                    StockPrice stockPrice = StockPrice.builder()
                            .stockCode(stockCode)
                            .stockName(String.valueOf(item.get("itmsNm")))
                            .basDd(String.valueOf(item.get("basDd")))
                            .clpr(Long.parseLong(String.valueOf(item.get("clpr"))))
                            .hipr(Long.parseLong(String.valueOf(item.get("hipr"))))
                            .lopr(Long.parseLong(String.valueOf(item.get("lopr"))))
                            .trqu(Long.parseLong(String.valueOf(item.get("trqu"))))
                            .mrktTotAmt(Long.parseLong(String.valueOf(item.get("mrktTotAmt"))))
                            .build();

                    stockPriceRepository.save(stockPrice);
                    updateMarketCap(stockCode, stockPrice.getMrktTotAmt());

                    System.out.println("====== [공공데이터 API 결과] ======");
                    System.out.println(response);
                    System.out.println("=====================================");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("공공데이터 API 파싱 실패: " + e.getMessage());
        }
    }



    /**
     * 2. 오픈다트 - 다중회사 주요계정(fnlttMultiAcnt) 가져와서 DB 저장
     *    사업보고서(11011) 기준 작년도 재무 데이터를 조회합니다.
     */
    @Transactional
    public void fetchAndSaveFinancialStatement(String stockCode) {
        String cleanStockCode = stockCode.trim();

        String corpCode = dartCorpCodeService.getCorpCode(cleanStockCode);
        if (corpCode == null) {
            System.out.println(">> [알림] " + cleanStockCode + " 종목의 DART corp_code 매핑을 찾을 수 없어 재무정보 조회를 건너뜁니다.");
            return;
        }

        String bsnsYear = String.valueOf(Year.now().getValue() - 1); // 작년도 고정
        String reprtCode = "11011"; // 사업보고서 고정

        String url = UriComponentsBuilder.fromUriString("https://opendart.fss.or.kr/api/fnlttMultiAcnt.json")
                .queryParam("crtfc_key", openDartKey)
                .queryParam("corp_code", corpCode)
                .queryParam("bsns_year", bsnsYear)
                .queryParam("reprt_code", reprtCode)
                .build()
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        System.out.println("====== [오픈다트 주요계정 API 결과] ======");
        System.out.println(response);
        System.out.println("=====================================");

        if (response != null) {
            String status = String.valueOf(response.get("status"));

            if ("000".equals(status)) {
                List<Map<String, String>> list = (List<Map<String, String>>) response.get("list");
                if (list != null) {
                    for (Map<String, String> item : list) {
                        FinancialStatement statement = FinancialStatement.builder()
                                .stockCode(cleanStockCode)
                                .corpCode(corpCode)
                                .bsnsYear(bsnsYear)
                                .reprtCode(reprtCode)
                                .fsDiv(item.get("fs_div"))
                                .sjDiv(item.get("sj_div"))
                                .accountNm(item.get("account_nm"))
                                .thstrmAmount(parseAmount(item.get("thstrm_amount")))
                                .frmtrmAmount(parseAmount(item.get("frmtrm_amount")))
                                .build();

                        financialStatementRepository.save(statement);
                    }
                    System.out.println(">> " + cleanStockCode + " 종목 주요계정 DB 저장 완료 (건수: " + list.size() + ")");
                }
            } else {
                String message = String.valueOf(response.get("message"));
                System.out.println(">> [알림] 오픈다트 주요계정 저장 건너뜀 (상태코드: " + status + " / 메시지: " + message + ")");
            }
        }
    }

    // DART 금액 문자열(콤마 포함 가능)을 Long으로 변환합니다. 값이 없으면 null을 반환합니다.
    private Long parseAmount(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(raw.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 3. [동적 수집] 시가총액 기준 상위 100개 기업을 자동으로 판별하여 주가 및 재무정보 저장
     */
    public void fetchAndSaveTop100ByMarketCap() {
        // 💡 1단계: 오늘(또는 가장 최근 영업일)의 전체 상장 주식 정보를 가져오기 위한 URL 생성
        // 특정 종목코드를 지정하지 않고(likeSrtnCd 제외), 대량으로(numOfRows=3000) 요청합니다.
        URI uri = UriComponentsBuilder.fromUriString("http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo")
                .queryParam("serviceKey", publicDataKey)
                .queryParam("resultType", "json")
                .queryParam("numOfRows", "3000") // 국내 상장사 수가 약 2,500~2,600개이므로 3000건이면 전수 조사가 가능합니다.
                .build(true)
                .toUri();

        System.out.println(">>> [동적 수집] 전체 주식 데이터 다운로드 중...");
        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);

        try {
            if (response == null) return;

            Map<String, Object> responseBody = (Map<String, Object>) response.get("response");
            Map<String, Object> body = (Map<String, Object>) responseBody.get("body");
            Map<String, Object> items = (Map<String, Object>) body.get("items");
            List<Map<String, Object>> allStocks = (List<Map<String, Object>>) items.get("item");

            if (allStocks == null || allStocks.isEmpty()) {
                System.out.println(">>> 데이터를 받아오지 못했습니다. 공공데이터포털 점검 또는 서비스 키를 확인하세요.");
                return;
            }

            System.out.println(">>> 총 " + allStocks.size() + "개의 주식 데이터를 분석합니다.");

            // 💡 2단계: 시가총액(mrktTotAmt) 기준 정렬 및 상위 100개 추출
            List<Map<String, Object>> top100Stocks = allStocks.stream()
                    .filter(stock -> stock.get("mrktTotAmt") != null) // 시가총액 데이터가 불완전한 항목 제외
                    .sorted((s1, s2) -> {
                        // 시가총액 값이 문자열로 들어오므로 Long으로 변환 후 역순(내림차순) 정렬
                        Long cap1 = Long.parseLong(String.valueOf(s1.get("mrktTotAmt")));
                        Long cap2 = Long.parseLong(String.valueOf(s2.get("mrktTotAmt")));
                        return cap2.compareTo(cap1); // s2와 s1의 위치를 바꾸면 내림차순이 됩니다.
                    })
                    .limit(100) // 딱 100개만 자르기
                    .collect(Collectors.toList());

            System.out.println(">>> 시가총액 상위 100개 기업 선별 완료. 수집 및 DB 적재를 시작합니다.");

            // 💡 3단계: 선별된 100개 기업을 순회하며 기존 적재 로직 실행
            for (int i = 0; i < top100Stocks.size(); i++) {
                Map<String, Object> stock = top100Stocks.get(i);
                String stockCode = String.valueOf(stock.get("srtnCd")); // 6자리 단축코드 추출
                String stockName = String.valueOf(stock.get("itmsNm"));

                System.out.println(String.format("[%d/100] %s(%s) 처리 중...", (i + 1), stockName, stockCode));

                try {
                    // A. 공공데이터 주가 저장 (이미 리스트에 데이터가 있으므로 API를 다시 쏠 필요 없이 바로 빌드해서 저장)
                    StockPrice stockPrice = StockPrice.builder()
                            .stockCode(stockCode)
                            .stockName(stockName)
                            .basDd(String.valueOf(stock.get("basDd")))
                            .clpr(Long.parseLong(String.valueOf(stock.get("clpr"))))
                            .hipr(Long.parseLong(String.valueOf(stock.get("hipr"))))
                            .lopr(Long.parseLong(String.valueOf(stock.get("lopr"))))
                            .trqu(Long.parseLong(String.valueOf(stock.get("trqu"))))
                            .mrktTotAmt(Long.parseLong(String.valueOf(stock.get("mrktTotAmt"))))
                            .build();

                    stockPriceRepository.save(stockPrice);
                    updateMarketCap(stockCode, stockPrice.getMrktTotAmt());

                    // B. 오픈다트 주요계정(재무정보) 연동
                    fetchAndSaveFinancialStatement(stockCode);

                    // 🔥 트래픽 과부하 방지를 위한 최소한의 디레이 (오픈다트 연속 호출 안정성 확보)
                    Thread.sleep(400);

                } catch (Exception e) {
                    System.err.println(">> [" + stockName + "] 처리 중 건별 오류 발생: " + e.getMessage());
                }
            }

            System.out.println(">>> [성공] 시가총액 상위 100개 대량 동적 수집 공정이 완수되었습니다!");

        } catch (Exception e) {
            throw new RuntimeException("전체 주식 파싱 및 정렬 실패: " + e.getMessage());
        }
    }
}