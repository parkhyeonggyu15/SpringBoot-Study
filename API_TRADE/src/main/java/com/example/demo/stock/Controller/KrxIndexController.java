package com.example.demo.stock.Controller;

import com.example.demo.stock.Entity.KospiIndexPrice;
import com.example.demo.stock.Service.KrxIndexApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/krx")
@RequiredArgsConstructor
public class KrxIndexController {

    private final KrxIndexApiService krxIndexApiService;

    /**
     * KRX KOSPI 시리즈 일별시세정보를 가져와 DB에 저장하고 결과를 반환합니다.
     * 예: GET http://localhost:8080/api/krx/kospi?basDd=20260617 (basDd 미입력 시 오늘 날짜로 조회)
     */
    @GetMapping("/kospi")
    public ResponseEntity<List<KospiIndexPrice>> getKospiSeries(
            @RequestParam(required = false) String basDd) {

        String searchDate = (basDd != null && !basDd.isBlank())
                ? basDd
                : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return ResponseEntity.ok(krxIndexApiService.fetchAndSaveKospiSeries(searchDate));
    }
}
