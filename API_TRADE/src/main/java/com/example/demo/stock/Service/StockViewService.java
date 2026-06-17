package com.example.demo.stock.Service;

import com.example.demo.stock.Dtos.StockDetailResponse;
import com.example.demo.stock.Entity.StockPrice;
import com.example.demo.stock.Repository.StockPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockViewService {

    private final StockPriceRepository stockPriceRepository;

    public StockDetailResponse getStockDashboardData(String stockCode) {
        // 1. DB에서 가장 최신 시세 정보 조회
        StockPrice price = stockPriceRepository.findFirstByStockCodeOrderByBasDdDesc(stockCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 종목의 시세 정보가 DB에 없습니다."));

        // 2. DTO 매핑 작업
        StockDetailResponse.PriceInfo priceInfo = new StockDetailResponse.PriceInfo(
                price.getBasDd(), price.getClpr(), price.getHipr(), price.getLopr(), price.getTrqu()
        );

        return new StockDetailResponse(price.getStockCode(), price.getStockName(), priceInfo);
    }
}
