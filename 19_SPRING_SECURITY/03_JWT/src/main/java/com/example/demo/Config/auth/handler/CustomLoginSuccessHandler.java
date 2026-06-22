package com.example.demo.Config.auth.handler;

import com.example.demo.Config.auth.jwt.JWTProperties;
import com.example.demo.Config.auth.jwt.JWTTokenProvider;
import com.example.demo.Config.auth.jwt.TokenInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {


    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //TOKEN을 COOKIE로 전달
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        Cookie cookie = new Cookie(JWTProperties.ACCESS_TOKEN_COOKIE_NAME,tokenInfo.getAccessToken());
        //[수정] Cookie.setMaxAge()는 '초' 단위이나 상수는 '밀리초'이므로 1000으로 나눠 전달 (5분)
        cookie.setMaxAge(JWTProperties.ACCESS_TOKEN_EXPIRATION_TIME/1000);  //accesstoken 유지시간
        cookie.setPath("/");    //쿠키 적용경로(/ : 모든경로)
        //[수정] JS에서 토큰 접근 차단(XSS 탈취 방지). HTTPS 환경에서는 cookie.setSecure(true)도 함께 권장
        cookie.setHttpOnly(true);
        response.addCookie(cookie); //응답정보에 쿠키 포함


        log.info("CustomSuccessHandler's onAuthenticationSuccess invoke...");
//        response.sendRedirect("/");
        String redirectUrl = "/";
        for(GrantedAuthority authority : authentication.getAuthorities())
        {
            log.info("authority : " + authority);
            String role = authority.getAuthority(); //String
            if(role.contains("ROLE_ADMIN")){
                // /admin 리다이렉트
                redirectUrl = "/admin";
                break;
            }else if(role.contains("ROLE_MANAGER")){
                // /manager 리다이렉트
                redirectUrl = "/manager";
                break;
            }else{
                // /user 리다이렉트
                redirectUrl = "/user";
                break;
            }
        }
        response.sendRedirect(redirectUrl);
    }
}
