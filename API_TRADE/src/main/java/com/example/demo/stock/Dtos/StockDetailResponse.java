package com.example.demo.stock.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockDetailResponse {
    private String stockCode;
    private String stockName;
    private PriceInfo priceInfo;

    @Getter
    @AllArgsConstructor
    public static class PriceInfo {
        private String baseDate;
        private Long closePrice;
        private Long highPrice;
        private Long lowPrice;
        private Long volume;
    }
}