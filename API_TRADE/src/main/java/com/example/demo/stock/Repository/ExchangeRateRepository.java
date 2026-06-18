package com.example.demo.stock.Repository;

import com.example.demo.stock.Entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findFirstByCountryOrderByBaseDateDesc(String country); // 국가별 가장 최근 환율 조회
}
