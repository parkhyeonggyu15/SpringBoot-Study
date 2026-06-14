package com.example.demo.stock.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StockPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockCode;   // 종목코드 (예: 005930)
    private String stockName;   // 종목명
    private String basDd;       // 기준일자 (YYYYMMDD)
    private Long clpr;          // 종가
    private Long hipr;          // 고가
    private Long lopr;          // 저가
    private Long trqu;          // 거래량
}
