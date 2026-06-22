package com.example.demo.Config.auth.handler;

import com.example.demo.Config.auth.jwt.JWTProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("CustomLogoutSuccessHandler's onLogoutSuccess invoke...");

        String token = null;

        Cookie[] cookies = request.getCookies();
        if (cookies!=null)
        {
            token = Arrays.stream(cookies)
                    .filter((cookie)->{return cookie.getName().equals(JWTProperties.ACCESS_TOKEN_COOKIE_NAME);})
                    .findFirst()
                    .map((cookie)->{return cookie.getValue();})
                    .orElse(null);
        }

        if(token!=null) {
            //access-token 쿠키 제거
            Cookie cookie = new Cookie(JWTProperties.ACCESS_TOKEN_COOKIE_NAME, null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        response.sendRedirect("/login?logout="+ URLEncoder.encode("로그아웃 성공","utf-8"));
    }
}
