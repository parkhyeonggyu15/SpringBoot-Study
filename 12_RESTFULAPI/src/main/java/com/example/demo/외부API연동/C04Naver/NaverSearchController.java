package com.example.demo.외부API연동.C04Naver;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
@RequestMapping("/Naver/search")
public class NaverSearchController {

    private String CLIENT_ID = "";
    private String CLIENT_SECRET ="";

    @GetMapping("/book/{keyword}")
    @ResponseBody
    public String search(@PathVariable String keyword)
    {
        log.info("GET /Naver.search...{}",keyword);

        //요청파라미터 정리
        String url = "https://openapi.naver.com/v1/search/book.json?query="+keyword;

        //요청 헤더
        HttpHeaders header = new HttpHeaders();
        header.add("X-Naver-Client-Id",CLIENT_ID);
        header.add("X-Naver-Client-Secret",CLIENT_SECRET);


//        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
//        params.add("query",keyword);



        //요청 엔터티(헤더 + 바디(params))
        HttpEntity entity = new HttpEntity(header);

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity,String.class);

        System.out.println(response.getBody());

        return response.getBody();
    }
}






