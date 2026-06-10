package com.example.demo.외부API연동.C03Kakao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/Kakao")
public class KakaoLoginController {

    private String CLIENT_ID="";
    private String REDIRECT_URI="http://127.0.0.1:8080/Kakao/callback";
    private String LOGOUT_REDIRECT_URI="";

    @GetMapping("/login")
    public String login(){
        log.info("GET /login...");
        return "redirect:https://kauth.kakao.com/oauth/authorize?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&response_type=code";
    }
    @GetMapping("/callback")
    public void callback(String code){
        log.info("GET /Kakao/callback..." + code);
    }

}
