package com.example.demo.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class MemoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //서브컨트롤러 진입전 처리코드
        log.info("[INTERCEPTOR] Memo컨트롤러 실행 전 : "+ request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //서브컨트롤러 작업 완료 이후(컨트롤러 핸들러 함수 실행 종료 , 뷰 랜더링 전)
        log.info("[INTERCEPTOR] Memo컨트롤러 실행 후 : "+ request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 요청 응답 완료이후(뷰 렌더링 이후)
        log.info("[INTERCEPTOR] Memo컨트롤러 요청-응답 완료 후 : "+ request.getRequestURI());
    }
}
