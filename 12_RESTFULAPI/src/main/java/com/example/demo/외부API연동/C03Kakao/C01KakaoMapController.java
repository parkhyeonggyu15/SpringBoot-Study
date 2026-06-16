package com.example.demo.외부API연동.C03Kakao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/Kakao")
public class C01KakaoMapController {

    @GetMapping("/map")
    public void main(){
        log.info("GET /kakao/map....");
    }
}
