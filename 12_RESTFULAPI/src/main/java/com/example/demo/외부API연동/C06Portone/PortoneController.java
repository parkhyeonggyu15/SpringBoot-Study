package com.example.demo.외부API연동.C06Portone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
@RequestMapping("/Portone")
public class PortoneController {
    String HOST ="https://api.iamport.kr";
    String IMP_KEY = "-";
    String IMP_SECRET  = "-";

    PortOneTokenResponse portOneTokenResponse;
    CertificationResponse certificationResponse;

    @GetMapping("/index")
    public void index(){
        log.info("GET /portone/index...");
    }

    private void getToken(){
        log.info("getToken invoke..");
        //요청파라미터 정리
        String url = HOST + "/users/getToken";

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
//        header.add("Authorization","Bearer "+kakaoTokenResponse.getAccess_token());
//        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        //요청 바디(파라미터)
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("imp_key",IMP_KEY);
        params.add("imp_secret",IMP_SECRET);

        //요청 엔터티(헤더 + 바디(params))
        HttpEntity< MultiValueMap<String,String> > entity = new HttpEntity<>(params,header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PortOneTokenResponse> response = restTemplate.exchange(url, HttpMethod.POST,entity,PortOneTokenResponse.class);
        System.out.println(response.getBody());
        this.portOneTokenResponse = response.getBody();
    }

    @GetMapping("/certifications/{imp_uid}")
    @ResponseBody
    public ResponseEntity<CertificationResponse> certification(@PathVariable String imp_uid){
        log.info("GET /Portone/certifications/{}",imp_uid);
        getToken();

        //요청파라미터 정리
        String url = HOST + "/certifications/"+imp_uid;

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+portOneTokenResponse.getResponse().getAccess_token());
        header.add("Content-Type","application/json");

        //요청 바디(파라미터)
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        //요청 엔터티(헤더 + 바디(params))
        HttpEntity entity = new HttpEntity(header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CertificationResponse> response = restTemplate.exchange(url, HttpMethod.GET,entity,CertificationResponse.class);
        System.out.println(response.getBody());

        certificationResponse = response.getBody();

        return  ResponseEntity.status(HttpStatus.OK).body(certificationResponse);

    }

    @GetMapping("/payments/{merchant_uid}")
    public ResponseEntity<?> payments(@PathVariable String merchant_uid){
        //파라미터받기(생략)
        getToken();

        //요청파라미터 정리
        String url = HOST + "/payments?imp_uid[]=&merchant_uid[]="+merchant_uid;

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+portOneTokenResponse.getResponse().getAccess_token());
        header.add("Content-Type","application/json");

        //요청 바디(파라미터)
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        //요청 엔터티(헤더 + 바디(params))
        HttpEntity entity = new HttpEntity(header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
        System.out.println(response.getBody());


        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/payments/status/{payment_status}")
    @ResponseBody
    public void payments_all(@PathVariable String payment_status){
        getToken();

        if(payment_status == null) payment_status="all";

        log.info("GET /payments/status/{}",payment_status);
        //요청파라미터 정리
        String url = HOST + "/payments/status/"+payment_status;

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+portOneTokenResponse.getResponse().getAccess_token());
        header.add("Content-Type","application/json");

        //요청 바디(파라미터)
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("page","1");
        params.add("limit","20");


        //요청 엔터티(헤더 + 바디(params))
        HttpEntity< MultiValueMap<String,String> > entity = new HttpEntity(params,header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
        System.out.println(response.getBody());
    }

    @GetMapping("/payments/cancel")
    @ResponseBody
    public void payments_cancel(){
        log.info("GET /paymenets/cancel..");
        getToken();

        //요청파라미터 정리
        String url = HOST + "/payments/cancel";

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+portOneTokenResponse.getResponse().getAccess_token());
        header.add("Content-Type","application/json");

        //요청 바디(파라미터)
//        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        JSONObject params = new JSONObject();
        params.put("merchant_uid","test_merchand1781501552206");   //or merchant_uid

        //요청 엔터티(헤더 + 바디(params))
        HttpEntity< JSONObject > entity = new HttpEntity(params,header);


        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
        System.out.println(response.getBody());

    }

    //------------------------------------------
    @Data
    private static class TokenResponse{
        public String access_token;
        public int now;
        public int expired_at;
    }
    @Data
    private static class PortOneTokenResponse{
        public int code;
        public Object message;
        @JsonProperty("response")
        public TokenResponse response;
    }
    //-------------------------------------------
    @Data
    private static class CertResponse{
        public int birth;
        public String birthday;
        public boolean certified;
        public int certified_at;
        public boolean foreigner;
        public boolean foreigner_v2;
        public String gender;
        public String imp_uid;
        public String merchant_uid;
        public String name;
        public String origin;
        public String pg_provider;
        public String pg_tid;
        public String phone;
        public Object unique_in_site;
        public String unique_key;
    }
    @Data
    private static class CertificationResponse{
        public int code;
        public Object message;
        @JsonProperty("response")
        public CertResponse response;
    }


}
