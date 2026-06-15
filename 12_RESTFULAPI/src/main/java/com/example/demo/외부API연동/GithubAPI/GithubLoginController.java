package com.example.demo.외부API연동.GithubAPI;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/*
 * GitHub OAuth 소셜 로그인 실습
 * - 개인 계정 무료, OAuth App 등록만으로 사용 (사업자 불필요)
 *
 * [공식 문서]
 * - OAuth App 등록     : https://github.com/settings/developers
 * - 인가/토큰 흐름      : https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps
 * - 로그인 사용자 조회  : https://docs.github.com/en/rest/users/users#get-the-authenticated-user
 *
 * [확인 방법]
 *   OAuth App 의 Authorization callback URL 을 http://localhost:8099/github/callback 로 등록하고
 *   CLIENT_ID / CLIENT_SECRET 채운 뒤, 브라우저에서
 *   http://localhost:8099/github/login  →  깃허브 로그인 →  callback 에서 프로필 JSON 확인
 */
@Controller
@Slf4j
@RequestMapping("/github")
public class GithubLoginController {

    private String CLIENT_ID = "";       // GitHub OAuth App Client ID
    private String CLIENT_SECRET = "";   // GitHub OAuth App Client Secret
    private String REDIRECT_URI = "http://localhost:8099/github/callback";

    // 1) 인가(Authorize) 요청 - 깃허브 로그인 페이지로 리다이렉트
    // 문서: https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#1-request-a-users-github-identity
    @GetMapping("/login")
    public String login() {
        log.info("GET /github/login...");
        return "redirect:https://github.com/login/oauth/authorize"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&scope=read:user";
    }

    // 2) 콜백 - code 로 AccessToken 발급 → 사용자 정보 조회
    // 문서: https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#2-users-are-redirected-back-to-your-site-by-github
    @GetMapping("/callback")
    @ResponseBody
    public ResponseEntity<String> callback(String code) {
        log.info("GET /github/callback...code : " + code);

        RestTemplate restTemplate = new RestTemplate();

        // 2-1) AccessToken 발급 요청
        String tokenUrl = "https://github.com/login/oauth/access_token";

        HttpHeaders tokenHeader = new HttpHeaders();
        tokenHeader.add("Accept", "application/json"); // 미지정 시 form 형식으로 응답됨

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("code", code);
        params.add("redirect_uri", REDIRECT_URI);

        HttpEntity<MultiValueMap<String, String>> tokenEntity = new HttpEntity<>(params, tokenHeader);

        ResponseEntity<String> tokenResponse =
                restTemplate.exchange(tokenUrl, HttpMethod.POST, tokenEntity, String.class);
        System.out.println("token : " + tokenResponse.getBody());

        // 응답 JSON({"access_token":"...","token_type":"bearer",...})에서 토큰 추출
        String accessToken = extractAccessToken(tokenResponse.getBody());

        // 2-2) 로그인 사용자 정보 조회
        // 문서: https://docs.github.com/en/rest/users/users#get-the-authenticated-user
        String userUrl = "https://api.github.com/user";

        HttpHeaders userHeader = new HttpHeaders();
        userHeader.add("Authorization", "Bearer " + accessToken);
        userHeader.add("Accept", "application/vnd.github+json");

        HttpEntity<String> userEntity = new HttpEntity<>(userHeader);

        ResponseEntity<String> userResponse =
                restTemplate.exchange(userUrl, HttpMethod.GET, userEntity, String.class);
        System.out.println("user : " + userResponse.getBody());

        return userResponse;
    }

    // access_token 값만 단순 추출(실습용)
    private String extractAccessToken(String json) {
        if (json == null) return "";
        String key = "\"access_token\":\"";
        int s = json.indexOf(key);
        if (s < 0) return "";
        s += key.length();
        int e = json.indexOf("\"", s);
        return json.substring(s, e);
    }
}
