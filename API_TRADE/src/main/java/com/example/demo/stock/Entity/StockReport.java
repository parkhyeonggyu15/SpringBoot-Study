package com.example.demo.stock.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StockReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockCode;       // 종목코드
    private String rceptNo;         // 공시 접수번호 (자연 키 역할)
    private String reportNm;        // 보고서명 (보고서 종류)
    private String rceptDt;         // 접수일자 (YYYYMMDD)
    private String bsnNm;           // 공시 대상 회사명
}