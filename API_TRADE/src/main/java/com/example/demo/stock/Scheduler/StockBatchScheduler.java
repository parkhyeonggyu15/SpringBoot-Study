package com.example.demo.stock.Scheduler;

import com.example.demo.stock.Service.StockApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockBatchScheduler {

    private final StockApiService stockApiService;

    // 평일 월~금 오후 6시 정각에 외부 데이터 수집 실행
    @Scheduled(cron = "0 0 18 * * MON-FRI")
//    @Scheduled(initialDelay = 1000, fixedDelay = 3600000)
    public void batchJob() {
        try {
            // 시가총액 상위 100개 기업의 주가/공시 데이터를 한 번에 수집
            stockApiService.fetchAndSaveTop100ByMarketCap();
        } catch (Exception e) {
            log.error("시가총액 상위 100개 기업 데이터 수집 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}