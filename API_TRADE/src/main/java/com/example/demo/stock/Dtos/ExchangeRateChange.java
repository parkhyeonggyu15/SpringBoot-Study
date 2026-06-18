package com.example.demo.stock.Dtos;

import com.example.demo.stock.Entity.ExchangeRate;
import lombok.Getter;

@Getter
public class ExchangeRateChange {
    private final String country;
    private final String curUnit;
    private final String curNm;
    private final String baseDate;
    private final Double dealBasR;
    private final String previousBaseDate;
    private final Double previousDealBasR;
    private final Double changeAmount;   // 전 영업일 대비 변동액
    private final Double changeRate;     // 전 영업일 대비 변동률(%)

    public ExchangeRateChange(ExchangeRate current, ExchangeRate previous, Double changeAmount, Double changeRate) {
        this.country = current.getCountry();
        this.curUnit = current.getCurUnit();
        this.curNm = current.getCurNm();
        this.baseDate = current.getBaseDate();
        this.dealBasR = current.getDealBasR();
        this.previousBaseDate = previous.getBaseDate();
        this.previousDealBasR = previous.getDealBasR();
        this.changeAmount = changeAmount;
        this.changeRate = changeRate;
    }
}
