package com.example.demo.외부API연동.C05Google;

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


/**
 * ======================================================================
 *  GoogleLoginController  -  구글 OAuth 2.0 로그인 (KakaoLoginController /
 *  NaverLoginController 와 동일한 패턴: login → getCode → getAccessToken → main)
 * ======================================================================
 *
 *  [기술 문서 DOC LINK]
 *  - OAuth 2.0 개요          : https://developers.google.com/identity/protocols/oauth2
 *  - 웹서버 앱 인증 흐름      : https://developers.google.com/identity/protocols/oauth2/web-server
 *  - OpenID Connect / scope  : https://developers.google.com/identity/protocols/oauth2/scopes
 *  - 사용자 정보(UserInfo)    : https://developers.google.com/identity/openid-connect/openid-connect#obtainuserinfo
 *  - 토큰 폐기(revoke)        : https://developers.google.com/identity/protocols/oauth2/web-server#tokenrevoke
 *  - 콘솔(클라이언트 발급)    : https://console.cloud.google.com/apis/credentials
 *
 *  ⚠️ 실습용 스켈레톤: CLIENT_ID / CLIENT_SECRET 에 구글 콘솔에서 발급받은 값을 채워야 동작합니다.
 *     리다이렉트 URI 는 콘솔의 "승인된 리디렉션 URI" 에 정확히 동일하게 등록되어 있어야 합니다.
 */


@Controller
@Slf4j
@RequestMapping("/google")
public class GoogleLoginController {

    private String CLIENT_ID = "=";
    private String CLIENT_SECRET = "=";
    private String REDIRECT_URI = "http://localhost:8080/google/callback";

    private String code;
    public static GoogleTokenResponse googleTokenResponse;

    /**
     * 1) 인가코드 요청 - 구글 로그인/동의 화면으로 redirect
     *    DOC: https://developers.google.com/identity/protocols/oauth2/web-server#creatingclient
     *    AUTH ENDPOINT: https://accounts.google.com/o/oauth2/v2/auth
     */
    @GetMapping("/login")
    public String login() {
        log.info("GET /google/login...");
        return "redirect:https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=code"
                + "&scope=https://www.googleapis.com/auth/userinfo.profile%20https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/calendar"
                + "&access_type=offline";
    }

    /**
     * 2) 인가코드 수신 → 토큰 발급으로 forward
     *    DOC: https://developers.google.com/identity/protocols/oauth2/web-server#handlingresponse
     */
    @GetMapping("/callback")
    public String getCode(String code) {
        log.info("GET /google/callback...code : " + code);
        this.code = code;
        return "forward:/google/getAccessToken";
    }

    /**
     * 3) 인가코드 → access_token 발급
     *    DOC: https://developers.google.com/identity/protocols/oauth2/web-server#exchange-authorization-code
     *    TOKEN ENDPOINT: https://oauth2.googleapis.com/token
     */
    @GetMapping("/getAccessToken")
    public String getAccessToken() {
        log.info("GET /google/getAccessToken....");

        String url = "https://oauth2.googleapis.com/token";
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 바디 파라미터
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, header);

        // 요청 후 응답 확인
        ResponseEntity<GoogleTokenResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, GoogleTokenResponse.class);
        System.out.println(response.getBody());

        this.googleTokenResponse = response.getBody();

        // main 으로 리다이렉트
        return "redirect:/google";
    }

    /**
     * 4) 사용자 프로필 조회 → 뷰로 전달
     *    DOC: https://developers.google.com/identity/openid-connect/openid-connect#obtainuserinfo
     *    USERINFO ENDPOINT(OIDC 표준): https://openidconnect.googleapis.com/v1/userinfo
     */
    @GetMapping
    public String main(Model model) {
        log.info("GET /google/index...");

        String url = "https://openidconnect.googleapis.com/v1/userinfo";
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정 (Bearer 토큰)
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + googleTokenResponse.getAccess_token());

        // 요청 바디 파라미터(X)
        HttpEntity entity = new HttpEntity(header);

        // 요청 후 응답 확인 (UserInfo 는 GET)
        ResponseEntity<GoogleProfileResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, GoogleProfileResponse.class);
        System.out.println(response.getBody());

        // 뷰로 전달
        GoogleProfileResponse profile = response.getBody();
        model.addAttribute("profile", profile);
        model.addAttribute("name", profile.getName());
        model.addAttribute("email", profile.getEmail());
        model.addAttribute("image_url", profile.getPicture());

        return "google/index";
    }

    /**
     * 5) 로그아웃(토큰 폐기) - access_token 또는 refresh_token 무효화
     *    DOC: https://developers.google.com/identity/protocols/oauth2/web-server#tokenrevoke
     *    REVOKE ENDPOINT: https://oauth2.googleapis.com/revoke
     */
    @GetMapping("/logout1")
    @ResponseBody
    public void logout() {
        log.info("GET /google/logout1...");

        String url = "https://oauth2.googleapis.com/revoke";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", googleTokenResponse.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, header);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
    }

    @GetMapping("/logout2")
    public String logout2(){
        //https://accounts.google.com/Logout
        log.info("GET /google/logout2....");
        return "redirect:https://accounts.google.com/Logout";
    }

    //---------------------------------------
    // GOOGLE ACCESS TOKEN CLASS
    // DOC: https://developers.google.com/identity/protocols/oauth2/web-server#exchange-authorization-code
    //---------------------------------------
    @Data
    public static class GoogleTokenResponse {
        public String access_token;
        public String expires_in;
        public String refresh_token;
        public String scope;
        public String token_type;
        public String id_token;
    }

    //---------------------------------------
    // GOOGLE PROFILE(UserInfo - OIDC Standard Claims) CLASS
    // DOC: https://developers.google.com/identity/openid-connect/openid-connect#obtainuserinfo
    //---------------------------------------
    @Data
    private static class GoogleProfileResponse {
        public String sub;            // 사용자 고유 ID (OIDC 표준: id → sub)
        public String email;
        public boolean email_verified; // OIDC 표준: verified_email → email_verified
        public String name;
        public String given_name;
        public String family_name;
        public String picture;
        public String locale;
    }
}