package com.example.demo.stock.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FinancialStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockCode;    // 종목코드
    private String corpCode;     // DART 고유번호
    private String bsnsYear;     // 사업연도 (예: 2025)
    private String reprtCode;    // 보고서 코드 (예: 11011 사업보고서)
    private String fsDiv;        // 개별/연결 구분 (OFS/CFS)
    private String sjDiv;        // 재무제표 구분 (BS/IS 등)
    private String accountNm;    // 계정명 (예: 자산총계, 매출액)
    private Long thstrmAmount;   // 당기 금액
    private Long frmtrmAmount;   // 전기 금액
}
