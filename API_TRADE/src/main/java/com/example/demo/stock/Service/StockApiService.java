package com.example.demo.stock.Service;

import com.example.demo.stock.Entity.StockPrice;
import com.example.demo.stock.Entity.StockReport;
import com.example.demo.stock.Repository.StockPriceRepository;
import com.example.demo.stock.Repository.StockReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate; // 💡 변경: RestTemplate 임포트
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StockApiService {

    private final StockPriceRepository stockPriceRepository;
    private final StockReportRepository stockReportRepository;

    // 💡 Netty 대신 자바 기본 보안 설정을 100% 따르는 RestTemplate을 선언합니다.
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${PublicDataKey}")
    private String publicDataKey;

    @Value("${OpenDartKey}")
    private String openDartKey;

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
                            .build();

                    stockPriceRepository.save(stockPrice);

                    System.out.println("====== [공공데이터 API 진짜 결과] ======");
                    System.out.println(response);
                    System.out.println("=====================================");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("공공데이터 API 파싱 실패: " + e.getMessage());
        }
    }

    @Transactional
    public void fetchAndSaveStockReports(String stockCode) {
        String cleanStockCode = stockCode.trim();

        // 💡 [수정] 오픈다트 표준 규격에 맞춰 대소문자와 주소 매핑을 교정했습니다.
        // 오픈다트는 기본적으로 개발자 편의를 위해 결과 포맷을 지정하는 형식을 취합니다.
        String url = "https://opendart.fss.or.kr/api/majorStock.xml?crtfc_key=" + openDartKey + "&corp_code=" + cleanStockCode;

        // 만약 위 주소로도 안 될 경우를 대비해, 오픈다트 공식 가이드의 정석 URL 규격으로 매핑합니다.
        // 오픈다트의 대량보유상황보고서 정식 API 경로는 대소문자를 구분할 수 있으므로 아래와 같이 검증합니다.
        String fixedUrl = UriComponentsBuilder.fromUriString("https://opendart.fss.or.kr/api/majorStock.json")
                .queryParam("crtfc_key", openDartKey)
                .queryParam("corp_code", cleanStockCode)
                .build()
                .toUriString();

        // API 호출
        Map<String, Object> response = restTemplate.getForObject(fixedUrl, Map.class);

        System.out.println("====== [오픈다트 API 진짜 결과] ======");
        System.out.println(response);
        System.out.println("=====================================");

        if (response != null) {
            String status = String.valueOf(response.get("status"));

            if ("000".equals(status)) {
                List<Map<String, String>> list = (List<Map<String, String>>) response.get("list");
                if (list != null) {
                    for (Map<String, String> item : list) {
                        StockReport report = StockReport.builder()
                                .stockCode(cleanStockCode)
                                .rceptNo(item.get("rcept_no"))
                                .reportNm(item.get("report_nm"))
                                .rceptDt(item.get("rcept_dt"))
                                .bsnNm(item.get("corp_nm"))
                                .build();

                        stockReportRepository.save(report);
                    }
                    System.out.println(">> " + cleanStockCode + " 종목 지분공시 DB 저장 완료 (건수: " + list.size() + ")");
                }
            } else {
                String message = String.valueOf(response.get("message"));
                System.out.println(">> [알림] 오픈다트 저장 건너뜀 (상태코드: " + status + " / 메시지: " + message + ")");
            }
        }
    }
}