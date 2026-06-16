package com.example.demo.외부API연동.C03Kakao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/Kakao/channel")
public class C03KakaoChannelController {

    @GetMapping
    public void channel(){
        log.info("GET /Kakao/channel...");
    }
}
