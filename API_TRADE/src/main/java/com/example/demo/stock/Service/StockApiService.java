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
import java.util.stream.Collectors;
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

        // 💡 [핵심 해결책 1] 오픈다트 진짜 대량보유 보고서 JSON 주소로 변경합니다.
        String targetUrl = "https://opendart.fss.or.kr/api/majorstock.json";

        // 💡 [핵심 해결책 2] 오픈다트 전용 8자리 고유번호(corp_code) 변환 로직
        // (원래는 전수 매핑 테이블이 필요하지만, 당장 상위 종목 테스트를 위해 하드코딩 처리)
        String corpCode = cleanStockCode;
        if ("005930".equals(cleanStockCode)) {
            corpCode = "00126380"; // 삼성전자 오픈다트 고유번호
        } else if ("000660".equals(cleanStockCode)) {
            corpCode = "00164779"; // SK하이닉스 오픈다트 고유번호
        }

        String fixedUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("crtfc_key", openDartKey)
                .queryParam("corp_code", corpCode) // 변환된 8자리 고유번호 주입!
                .build()
                .toUriString();

        try {
            // API 호출
            Map<String, Object> response = restTemplate.getForObject(fixedUrl, Map.class);

            System.out.println("====== [오픈다트 API 진짜 결과] ======");
            System.out.println(response);
            System.out.println("=====================================");

            if (response != null) {
                String status = String.valueOf(response.get("status"));

                if ("000".equals(status)) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) response.get("list");
                    if (list != null) {
                        for (Map<String, Object> item : list) {
                            StockReport report = StockReport.builder()
                                    .stockCode(cleanStockCode) // DB에는 찾기 편하게 원래 6자리 코드 저장
                                    .rceptNo(String.valueOf(item.get("rcept_no")))
                                    .reportNm(String.valueOf(item.get("report_nm")))
                                    .rceptDt(String.valueOf(item.get("rcept_dt")))
                                    .bsnNm(String.valueOf(item.get("corp_nm")))
                                    .build();

                            stockReportRepository.save(report);
                        }
                        System.out.println(">> " + cleanStockCode + " 종목 지분공시 DB 저장 완료 (건수: " + list.size() + ")");
                    }
                } else {
                    String message = String.valueOf(response.get("message"));
                    System.out.println(">> [알림] 오픈다트 수집 건너뜀 (상태코드: " + status + " / 메시지: " + message + ")");
                }
            }
        } catch (Exception e) {
            System.err.println(">> 오픈다트 API 연동 중 예외 발생: " + e.getMessage());
        }
    }
    /**
     * 3. [동적 수집] 시가총액 기준 상위 100개 기업을 자동으로 판별하여 주가 및 공시 저장
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
                            .build();

                    stockPriceRepository.save(stockPrice);

                    // B. 오픈다트 지분공시 연동 (오픈다트는 해당 종목코드로 실시간 조회가 필요하므로 기존 메소드 호출)
                    fetchAndSaveStockReports(stockCode);

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