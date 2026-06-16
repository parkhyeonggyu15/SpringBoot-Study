package com.example.demo.외부API연동.C01OpenData;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@Slf4j
@RequestMapping("/Open/DgIncident")
public class OpenData_03_Controller {
    private String server="https://apis.data.go.kr/6270000/service/rest/dgincident";
    private String serviceKey="33e4bf86c86594889a21fd30f3c30747357e7e594e5c71fd78f0fc6db5b2f956";
    private String pageNo = "1";
    private String numOfRows = "10";

    @GetMapping
    public void t1(){
        //요청 파라미터 준비
        URI uri = UriComponentsBuilder.fromHttpUrl(server)
                .queryParam("serviceKey", serviceKey)  // 디코딩 키
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .encode()                              // 한 번만 인코딩
                .build()
                .toUri();
        log.info("GET /Open/DgIncident..."+uri);

        //요청 헤더(x)

        //요청 바디(x)

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(uri, HttpMethod.GET,null, String.class);

        //응답 변환 처리
        System.out.println(response.getBody());
    }



}
