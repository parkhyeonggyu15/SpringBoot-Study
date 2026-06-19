package com.example.demo.Config.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
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
