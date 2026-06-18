package com.example.demo.stock.Repository;

import com.example.demo.stock.Entity.KospiIndexPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface KospiIndexPriceRepository extends JpaRepository<KospiIndexPrice, Long> {
    List<KospiIndexPrice> findByBasDd(String basDd); // 기준일자별 KOSPI 시리즈 전체 조회
    Optional<KospiIndexPrice> findFirstByIdxNmOrderByBasDdDesc(String idxNm); // 지수별 가장 최근 시세 조회
}
