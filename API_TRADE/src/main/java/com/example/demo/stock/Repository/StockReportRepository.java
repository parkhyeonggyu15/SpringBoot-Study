package com.example.demo.stock.Repository;

import com.example.demo.stock.Entity.StockReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockReportRepository extends JpaRepository<StockReport, Long> {
    List<StockReport> findTop5ByStockCodeOrderByRceptDtDesc(String stockCode); // 최근 공시 5건 조회
}
