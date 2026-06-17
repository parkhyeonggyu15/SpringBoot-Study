package com.example.demo.stock.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "corp_code_mapping", uniqueConstraints = @UniqueConstraint(columnNames = "stock_code"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CorpCodeMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_code", nullable = false, length = 6)
    private String stockCode;   // KRX 종목코드 (예: 005930)

    @Column(name = "corp_code", nullable = false, length = 8)
    private String corpCode;    // DART 고유번호 (예: 00126380)

    private String corpName;    // 회사명

    private long mrktTotAmt;    // 시가총액 (조 단위까지 들어가므로 int 범위를 넘어 long 사용)

    public void updateMrktTotAmt(long mrktTotAmt) {
        this.mrktTotAmt = mrktTotAmt;
    }
}
