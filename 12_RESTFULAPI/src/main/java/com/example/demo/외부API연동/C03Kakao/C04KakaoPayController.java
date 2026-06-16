package com.example.demo.외부API연동.C03Kakao;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
@RequestMapping("/Kakao/pay")
public class C04KakaoPayController {

    private String SECRET_KEY="-";

    @GetMapping
    @ResponseBody
    public void req(){
        log.info("GET /Kakao/pay...");

        //요청파라미터 정리
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","SECRET_KEY "+SECRET_KEY);
        header.add("Content-Type","application/json");

        //요청파라미터 정리
        JSONObject params = new JSONObject();
        params.put("cid","TC0ONETIME");
        params.put("partner_order_id","shop_1111");
        params.put("partner_user_id","shop_1111_user1");
        params.put("item_name","초코파이");
        params.put("quantity","1");
        params.put("total_amount","2200");
        params.put("tax_free_amount","200");
        params.put("approval_url","http://127.0.0.1:8080/Kakao/pay/success");
        params.put("cancel_url","http://127.0.0.1:8080/Kakao/pay/cancel");
        params.put("fail_url","http://127.0.0.1:8080/Kakao/pay/fail");



        //요청 엔터티(헤더 + 바디(params))
        HttpEntity< JSONObject > entity = new HttpEntity<>(params,header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);

        System.out.println(response.getBody());
    }


    @GetMapping("/success")
    @ResponseBody
    public void success(){log.info("GET /Kakao.success.... 결제성공후 이동되는 위치");}
    @GetMapping("/cancel")
    @ResponseBody
    public void cancel(){log.info("GET /Kakao.cancel.... 결제취소시 이동되는 위치");}
    @GetMapping("/fail")
    @ResponseBody
    public void fail(){log.info("GET /Kakao.fail.... 결제실패시 이동되는 위치");}
}
