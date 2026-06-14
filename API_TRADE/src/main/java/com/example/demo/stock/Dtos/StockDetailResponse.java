package com.example.demo.stock.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StockDetailResponse {
    private String stockCode;
    private String stockName;
    private PriceInfo priceInfo;
    private List<ReportInfo> recentReports;

    @Getter
    @AllArgsConstructor
    public static class PriceInfo {
        private String baseDate;
        private Long closePrice;
        private Long highPrice;
        private Long lowPrice;
        private Long volume;
    }

    @Getter
    @AllArgsConstructor
    public static class ReportInfo {
        private String rceptNo;
        private String reportName;
        private String receiptDate;
    }
}