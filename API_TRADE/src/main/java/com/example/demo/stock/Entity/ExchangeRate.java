package com.example.demo.stock.Entity;
import jakarta.persistence.*;
import lombok.*;

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
    private Double changeAmount; // 전 영업일 대비 변동액
    private Double changeRate;   // 전 영업일 대비 변동률(%)

    public void updateChange(Double changeAmount, Double changeRate) {
        this.changeAmount = changeAmount;
        this.changeRate = changeRate;
    }
}
