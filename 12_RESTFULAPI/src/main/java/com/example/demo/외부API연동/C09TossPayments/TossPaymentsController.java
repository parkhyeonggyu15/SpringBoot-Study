package com.example.demo.외부API연동.C09TossPayments;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

/*
 * 토스페이먼츠 결제 실습 - 결제 승인 / 조회 (테스트 키)
 * - 가입만으로 테스트 시크릿 키(test_sk_...) 제공 → 실결제 없이 전 과정 테스트 (사업자 불필요)
 *
 * [공식 문서]
 * - 개발자센터(키)  : https://developers.tosspayments.com/
 * - 인증(Basic)     : https://docs.tosspayments.com/reference/using-api/authorization
 * - 결제 승인 API   : https://docs.tosspayments.com/reference#결제-승인
 * - 결제 조회 API   : https://docs.tosspayments.com/reference#paymentkey로-결제-조회
 *
 * [확인 방법]
 *   SECRET_KEY(test_sk_...) 채운 뒤, 결제위젯에서 받은 값으로
 *   http://localhost:8099/toss/confirm?paymentKey=...&orderId=...&amount=1000
 *   http://localhost:8099/toss/payment/{paymentKey}
 */
@RestController
@Slf4j
@RequestMapping("/toss")
public class TossPaymentsController {

    private String SECRET_KEY = "";   // 토스페이먼츠 테스트 시크릿 키 (test_sk_...)

    private final String HOST = "https://api.tosspayments.com";

    // 결제 승인 (결제위젯/결제창에서 받은 paymentKey, orderId, amount 로 최종 승인)
    // 문서: https://docs.tosspayments.com/reference#결제-승인
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(
            @RequestParam("paymentKey") String paymentKey,
            @RequestParam("orderId") String orderId,
            @RequestParam("amount") int amount
    ) {
        log.info("GET /toss/confirm...orderId : " + orderId + ", amount : " + amount);

        String url = HOST + "/v1/payments/confirm";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 - Basic 인증 : base64(secretKey + ":")
        // 문서: https://docs.tosspayments.com/reference/using-api/authorization
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Basic " + basicAuth());
        header.add("Content-Type", "application/json");

        // 요청 바디
        JSONObject params = new JSONObject();
        params.put("paymentKey", paymentKey);
        params.put("orderId", orderId);
        params.put("amount", amount);

        HttpEntity<String> entity = new HttpEntity<>(params.toJSONString(), header);

        // 요청 후 응답 확인
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());

        return response;
    }

    // 결제 조회 (paymentKey 로 단건 조회)
    // 문서: https://docs.tosspayments.com/reference#paymentkey로-결제-조회
    @GetMapping("/payment/{paymentKey}")
    public ResponseEntity<String> getPayment(@PathVariable("paymentKey") String paymentKey) {
        log.info("GET /toss/payment...paymentKey : " + paymentKey);

        String url = HOST + "/v1/payments/" + paymentKey;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Basic " + basicAuth());

        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println(response.getBody());

        return response;
    }

    // 시크릿 키 뒤에 ':' 를 붙여 base64 인코딩 (비밀번호 없음)
    private String basicAuth() {
        return Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes());
    }
}
