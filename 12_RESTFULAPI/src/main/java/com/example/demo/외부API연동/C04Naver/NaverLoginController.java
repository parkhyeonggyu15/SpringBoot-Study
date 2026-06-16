package com.example.demo.외부API연동.C04Naver;

import com.example.demo.외부API연동.C03Kakao.C02KakaoLoginController;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
@RequestMapping("/Naver")
public class NaverLoginController {

    private String CLIENT_ID = "-";
    private String CLIENT_SECRET ="-";
    private String CALLBACK_URL="http://127.0.0.1:8080/Naver/callback";

    private NaverTokenResponse naverTokenResponse;
    private NaverProfileResponse naverProfileResponse;

    @GetMapping("/login")
    public String login(){
        log.info("GET /login....");
        return "redirect:https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="+CLIENT_ID+"&state=STATE_STRING&redirect_uri="+CALLBACK_URL;
    }

    @GetMapping("/callback")
    public String callback(String code, String state,String error , String error_description){
        log.info("GET /Naver/callback...{},{},{},{}" ,code,state,error,error_description);


        //요청파라미터 정리
        String url = "https://nid.naver.com/oauth2.0/token";

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id",CLIENT_ID );
        params.add("client_secret",CLIENT_SECRET);
        params.add("code",code);
        params.add("state",state);

        //요청 헤더
        HttpHeaders header = new HttpHeaders();

        //요청 엔터티(헤더 + 바디(params))
        HttpEntity< MultiValueMap<String,String> > entity = new HttpEntity<>(params,header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NaverTokenResponse> response = restTemplate.exchange(url, HttpMethod.POST,entity,NaverTokenResponse.class);

        System.out.println(response.getBody());
        this.naverTokenResponse = response.getBody();

        return "redirect:/Naver";


    }
    //Endpoint : /Naver
    @GetMapping
    public String main(Model model){
        log.info("GET /Naver...");

        //요청파라미터 정리
        String url = "https://openapi.naver.com/v1/nid/me";

//        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
//        params.add("","");
//        params.add("","");
//        params.add("","");
//        params.add("","");

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+naverTokenResponse.getAccess_token());

        //요청 엔터티(헤더 + 바디(params))
        HttpEntity entity = new HttpEntity(header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NaverProfileResponse> response = restTemplate.exchange(url, HttpMethod.POST,entity, NaverProfileResponse.class);
        System.out.println(response.getBody());
        this.naverProfileResponse = response.getBody();

        //
        model.addAttribute("naverTokenResponse",naverTokenResponse);
        model.addAttribute("naverProfileResponse",naverProfileResponse);

        return "Naver/main";
    }



    @GetMapping("/logout")
    public String logout(){
        log.info("GET /Naver/logout");
        return "redirect:https://nid.naver.com/nidlogin.logout?returl=https://www.naver.com/";
    }

    //
    @Data
    private static class NaverTokenResponse{
        public String access_token;
        public String refresh_token;
        public String token_type;
        public String expires_in;
    }
    //
    @Data
    private static class Profile{
        public String id;
        public String nickname;
        public String email;
        public String name;
    }
    @Data
    private static class NaverProfileResponse{
        public String resultcode;
        public String message;
        @JsonProperty(value="response")
        public Profile profile;
    }


}
