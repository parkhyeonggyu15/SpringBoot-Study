package com.example.demo.stock.Controller;

import com.example.demo.stock.Dtos.ExchangeRateChange;
import com.example.demo.stock.Service.ExchangeRateApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateApiService exchangeRateApiService;

    /**
     * 한국/일본/미국/중국의 기준일자 환율과 전 영업일 대비 등락률을 함께 조회합니다.
     * 예: GET http://localhost:8080/api/exchange?date=20260618 (date 미입력 시 오늘 날짜로 조회)
     */
    @GetMapping
    public ResponseEntity<List<ExchangeRateChange>> getExchangeRates(
            @RequestParam(required = false) String date) {

        String searchDate = (date != null && !date.isBlank())
                ? date
                : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return ResponseEntity.ok(exchangeRateApiService.
                getExchangeRateChanges(searchDate));
    }
}
