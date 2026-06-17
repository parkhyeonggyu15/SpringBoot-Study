package com.example.demo.Filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;


@WebFilter("/memo/*")
@Slf4j
public class MemoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //전
        log.info("[FILTER] MemoFilter START");
        chain.doFilter(request,response);

        //후
        log.info("[FILTER] MemoFilter END");
    }
}
