package com.example.demo.stock.Scheduler;

import com.example.demo.stock.Service.StockApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StockBatchScheduler {

    private final StockApiService stockApiService;

    // 모니터링 타겟 종목 코드 목록 (예: 삼성전자, SK하이닉스 등)
    private final List<String> targetStocks = List.of("005930", "000660");

    // 평일 월~금 오후 6시 정각에 외부 데이터 수집 실행
//    @Scheduled(cron = "0 0 18 * * MON-FRI")
    @Scheduled(fixedDelay = 10000)
    public void batchJob() {
        for (String code : targetStocks) {
            try {
                stockApiService.fetchAndSaveStockPrice(code);
                stockApiService.fetchAndSaveStockReports(code);
            } catch (Exception e) {
                // 로그 적재 및 예외 처리
                System.err.println(code + " 데이터 수집 중 오류 발생: " + e.getMessage());
            }
        }
    }
}