package com.example.demo.stock.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;     // 국가 (KOREA/JAPAN/USA/CHINA)
    private String curUnit;      // 통화코드 (KRW/JPY(100)/USD/CNH)
    private String curNm;        // 통화명
    private String baseDate;     // 기준일자 (YYYYMMDD)
    private Double ttb;          // 전신환(송금) 받을 때 환율
    private Double tts;          // 전신환(송금) 보낼 때 환율
    private Double dealBasR;     // 매매기준율

    @Id
    @Column(name = "rate_date")
    private LocalDate date;

    @Column(name = "usd_rate", precision = 12, scale = 4)
    private BigDecimal usdRate;

    @Column(name = "usd_change_rate", precision = 8, scale = 2)
    private BigDecimal usdChangeRate;

    @Column(name = "jpy_rate", precision = 12, scale = 4)
    private BigDecimal jpyRate;

    @Column(name = "jpy_change_rate", precision = 8, scale = 2)
    private BigDecimal jpyChangeRate;

    @Column(name = "cny_rate", precision = 12, scale = 4)
    private BigDecimal cnyRate;

    @Column(name = "cny_change_rate", precision = 8, scale = 2)
    private BigDecimal cnyChangeRate;

    @Column(name = "eur_rate", precision = 12, scale = 4)
    private BigDecimal eurRate;

    @Column(name = "eur_change_rate", precision = 8, scale = 2)
    private BigDecimal eurChangeRate;

}
