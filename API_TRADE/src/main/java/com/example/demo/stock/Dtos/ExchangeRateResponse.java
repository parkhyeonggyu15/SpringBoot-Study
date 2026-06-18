package com.example.demo.stock.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeRateResponse {
    private String country;     // KOREA/JAPAN/USA/CHINA
    private String curUnit;     // 통화코드
    private String curNm;       // 통화명
    private String baseDate;    // 기준일자
    private Double dealBasR;    // 매매기준율 (원화 기준 1단위당 가격, 한국은 1.0)
}
