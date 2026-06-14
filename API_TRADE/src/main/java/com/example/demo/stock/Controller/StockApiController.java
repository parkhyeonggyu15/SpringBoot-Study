package com.example.demo.stock.Controller;

import com.example.demo.stock.Dtos.StockDetailResponse;
import com.example.demo.stock.Service.StockViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockApiController {

    private final StockViewService stockViewService;

    /**
     * 화면에서 특정 종목코드를 넘겨 호출하는 API
     * 예: GET http://localhost:8080/api/stock/005930
     */
    @GetMapping("/{stockCode}")
    public ResponseEntity<StockDetailResponse> getStockDetailData(@PathVariable String stockCode) {
        StockDetailResponse response = stockViewService.getStockDashboardData(stockCode);
        return ResponseEntity.ok(response);
    }
}