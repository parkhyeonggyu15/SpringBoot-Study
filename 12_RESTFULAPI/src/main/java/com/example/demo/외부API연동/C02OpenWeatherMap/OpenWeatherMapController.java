package com.example.demo.외부API연동.C02OpenWeatherMap;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/OpenWeather")
public class OpenWeatherMapController {
    private String server="https://api.openweathermap.org/data/2.5/weather";
    private String appid="b7a263e63bfe790ff0081e9b619e7c91";

    //동기요청방식(SPRINGBOOT 단독 FN + BN)
    @GetMapping("/index")
    public String page(){
        log.info("GET /OpenWeather...");
        return "OpenWeather/index";
    }


    //비동기요청방식(BN  / FN 분할)
    @ResponseBody
    @CrossOrigin(originPatterns = "*")
    @GetMapping("/{lat}/{lon}")
    public ResponseEntity<Map<String,Object>> t1(
            @PathVariable String lat,
            @PathVariable String lon
    ){
        Map<String,Object> responseData = new HashMap<>();

        log.info("GET /OepnWeather/{}/{}",lat,lon);
        URI uri = UriComponentsBuilder.fromHttpUrl(server)
                .queryParam("appid", appid)  // 디코딩 키
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .encode()                              // 한 번만 인코딩
                .build()
                .toUri();

        //요청 헤더(x)

        //요청 바디(x)

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Root> response =
                restTemplate.exchange(uri, HttpMethod.GET,null, Root.class);

        //응답 변환 처리
        System.out.println(response.getBody());

        responseData.put("weather",response.getBody().getWeather());
        responseData.put("main",response.getBody().getMain());
        responseData.put("wind",response.getBody().getWind());
        return ResponseEntity.status(HttpStatus.OK).body(responseData);

    }

    @Data
    private static class Clouds{
        public int all;
    }
    @Data
    private static class Coord{
        public double lon;
        public double lat;
    }
    @Data
    private static class Main{
        public double temp;
        public double feels_like;
        public double temp_min;
        public double temp_max;
        public int pressure;
        public int humidity;
        public int sea_level;
        public int grnd_level;
    }
    @Data
    private static class Root{
        public Coord coord;
        public ArrayList<Weather> weather;
        public String base;
        public Main main;
        public int visibility;
        public Wind wind;
        public Clouds clouds;
        public int dt;
        public Sys sys;
        public int timezone;
        public int id;
        public String name;
        public int cod;
    }
    @Data
    private static class Sys{
        public String country;
        public int sunrise;
        public int sunset;
    }
    @Data
    private static class Weather{
        public int id;
        public String main;
        public String description;
        public String icon;
    }
    @Data
    private static class Wind{
        public double speed;
        public int deg;
        public double gust;
    }


}
