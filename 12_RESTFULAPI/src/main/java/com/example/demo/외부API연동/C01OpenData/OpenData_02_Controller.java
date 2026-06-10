package com.example.demo.외부API연동.C01OpenData;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;


//대구광역시 시내버스 노선 정보 : https://www.data.go.kr/data/15156114/fileData.do
//노선ID      : 3000649000
//버스번호     : 649
//정류소ID    : 7001001600
@RestController
@Slf4j
@RequestMapping("/Open/Bus")
public class OpenData_02_Controller {
    //&bsId=7001001600&routeNo=649

    private String server="https://apis.data.go.kr/6270000/dbmsapi02/getRealtime02";
    private String serviceKey="33e4bf86c86594889a21fd30f3c30747357e7e594e5c71fd78f0fc6db5b2f956";
    private String bsId = "7001001600";
    private String routeNo = "649";
    @GetMapping("/{bsId}/{routeNo}")
    public void get(
            @PathVariable(value = "bsId", required = false) String bsId,
            @PathVariable(value = "routeNo" , required = false) String routeNo
    ){
        //요청 파라미터 준비
        URI uri = UriComponentsBuilder.fromHttpUrl(server)
                .queryParam("serviceKey", serviceKey)  // 디코딩 키
                .queryParam("bsId", bsId)
                .queryParam("routeNo", routeNo)
                .encode()                              // 한 번만 인코딩
                .build()
                .toUri();
        log.info("GET /Open/Bus..."+uri);

        //요청 헤더(x)

        //요청 바디(x)

        //응답 = 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Root> response =
                restTemplate.exchange(uri, HttpMethod.GET,null, Root.class);

        //응답 변환 처리
        System.out.println(response.getBody());
    }

    @Data
    private static class ArrList{
        public String routeId;
        public String routeNo;
        public String moveDir;
        public int bsGap;
        public String bsNm;
        public String vhcNo2;
        public String busTCd2;
        public String busTCd3;
        public String busAreaCd;
        public String arrState;
        public int prevBsGap;
        public int arrTime;
    }
    @Data
    private static class Body{
        public ArrayList<Item> items;
        public int totalCount;
    }
    @Data
    private static class Header{
        public boolean success;
        public String resultCode;
        public String resultMsg;
    }
    @Data
    private static class Item{
        public String routeNo;
        public ArrayList<ArrList> arrList;
    }
    @Data
    private static class Root{
        public Header header;
        public Body body;
    }
}
