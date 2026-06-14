package com.example.demo.stock.Repository;

import com.example.demo.stock.Entity.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
    Optional<StockPrice> findFirstByStockCodeOrderByBasDdDesc(String stockCode); // 가장 최근 시세 조회
}
