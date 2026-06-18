package com.example.demo.stock.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KospiIndexPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String basDd;          // 기준일자 (YYYYMMDD)
    private String idxNm;          // 지수명 (코스피, 코스피 200 등)
    private Double clsprcIdx;      // 종가
    private Double cmpprevddIdx;   // 대비
    private Double flucRt;         // 등락률
    private Double opnprcIdx;      // 시가
    private Double hgprcIdx;       // 고가
    private Double lwprcIdx;       // 저가
    private Long accTrdvol;        // 거래량
    private Long accTrdval;        // 거래금액
    private Long mktcap;           // 상장시가총액
}
