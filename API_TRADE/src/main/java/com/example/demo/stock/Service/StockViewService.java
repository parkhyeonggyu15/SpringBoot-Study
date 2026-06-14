package com.example.demo.stock.Service;

import com.example.demo.stock.Dtos.StockDetailResponse;
import com.example.demo.stock.Entity.StockPrice;
import com.example.demo.stock.Entity.StockReport;
import com.example.demo.stock.Repository.StockPriceRepository;
import com.example.demo.stock.Repository.StockReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockViewService {

    private final StockPriceRepository stockPriceRepository;
    private final StockReportRepository stockReportRepository;

    public StockDetailResponse getStockDashboardData(String stockCode) {
        // 1. DB에서 가장 최신 시세 정보 조회
        StockPrice price = stockPriceRepository.findFirstByStockCodeOrderByBasDdDesc(stockCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 종목의 시세 정보가 DB에 없습니다."));

        // 2. DB에서 최근 공시 정보 5건 조회
        List<StockReport> reports = stockReportRepository.findTop5ByStockCodeOrderByRceptDtDesc(stockCode);

        // 3. DTO 매핑 작업
        StockDetailResponse.PriceInfo priceInfo = new StockDetailResponse.PriceInfo(
                price.getBasDd(), price.getClpr(), price.getHipr(), price.getLopr(), price.getTrqu()
        );

        List<StockDetailResponse.ReportInfo> reportInfos = reports.stream()
                .map(r -> new StockDetailResponse.ReportInfo(r.getRceptNo(), r.getReportNm(), r.getRceptDt()))
                .collect(Collectors.toList());

        return new StockDetailResponse(price.getStockCode(), price.getStockName(), priceInfo, reportInfos);
    }
}
