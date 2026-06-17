package com.example.demo.stock.Repository;

import com.example.demo.stock.Entity.CorpCodeMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CorpCodeMappingRepository extends JpaRepository<CorpCodeMapping, Long> {
    Optional<CorpCodeMapping> findByStockCode(String stockCode);
}
