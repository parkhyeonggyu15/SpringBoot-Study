package com.example.demo.stock.Repository;

import com.example.demo.stock.Entity.FinancialStatement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialStatementRepository extends JpaRepository<FinancialStatement, Long> {
    List<FinancialStatement> findByStockCodeAndBsnsYearAndReprtCode(String stockCode, String bsnsYear, String reprtCode);
}
