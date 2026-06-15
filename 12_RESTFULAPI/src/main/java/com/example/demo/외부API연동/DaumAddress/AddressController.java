package com.example.demo.외부API연동.DaumAddress;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/*
 * 주소 검색 실습 - 카카오 주소검색 + 도로명주소(juso.go.kr)
 * - 둘 다 개인 무료 발급 (사업자 불필요)
 *
 * [공식 문서]
 * - 카카오 개발자(REST 키) : https://developers.kakao.com/
 * - 카카오 주소 검색 API   : https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-address
 * - 도로명주소 신청/문서   : https://business.juso.go.kr/addrlink/openApi/searchApi.do
 *
 * [확인 방법]
 *   KAKAO_REST_KEY 또는 JUSO_CONFM_KEY 채운 뒤
 *   http://localhost:8099/address/kakao?query=판교역로 235
 *   http://localhost:8099/address/juso?keyword=판교역로 235
 */
@RestController
@Slf4j
@RequestMapping("/address")
public class AddressController {

    private String KAKAO_REST_KEY = "";   // 카카오 REST API 키
    private String JUSO_CONFM_KEY = "";   // 도로명주소 승인키(confmKey)

    // 1) 카카오 주소 검색 (헤더 인증: Authorization: KakaoAK {REST_KEY})
    // 문서: https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-address
    @GetMapping("/kakao")
    public ResponseEntity<String> kakao(@RequestParam("query") String query) {
        log.info("GET /address/kakao...query : " + query);

        String url = UriComponentsBuilder
                .fromHttpUrl("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", query)
                .encode()
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "KakaoAK " + KAKAO_REST_KEY);

        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println(response.getBody());

        return response;
    }

    // 2) 도로명주소 검색 (쿼리 파라미터 인증: confmKey)
    // 문서: https://business.juso.go.kr/addrlink/openApi/searchApi.do
    @GetMapping("/juso")
    public ResponseEntity<String> juso(@RequestParam("keyword") String keyword) {
        log.info("GET /address/juso...keyword : " + keyword);

        String url = UriComponentsBuilder
                .fromHttpUrl("https://business.juso.go.kr/addrlink/addrLinkApi.do")
                .queryParam("confmKey", JUSO_CONFM_KEY)
                .queryParam("currentPage", 1)
                .queryParam("countPerPage", 10)
                .queryParam("keyword", keyword)
                .queryParam("resultType", "json")
                .encode()
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 x / 요청 바디 x
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        System.out.println(response.getBody());

        return response;
    }
}
