package com.example.demo.stock.Scheduler;

import com.example.demo.stock.Service.ExchangeRateApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateBatchScheduler {

    private final ExchangeRateApiService exchangeRateApiService;

    // 평일 월~금 오전 11시(한국수출입은행 환율 고시 시각 이후)에 한국/일본/미국/중국 환율 자동 수집
//    @Scheduled(cron = "0 0 11 * * MON-FRI")
//    public void batchJob() {
//        try {
//            exchangeRateApiService.fetchAndSaveExchangeRatesForToday();
//            log.info("환율 정보 자동 수집 완료");
//        } catch (Exception e) {
//            log.error("환율 정보 자동 수집 중 오류 발생: {}", e.getMessage(), e);
//        }
//    }
}
