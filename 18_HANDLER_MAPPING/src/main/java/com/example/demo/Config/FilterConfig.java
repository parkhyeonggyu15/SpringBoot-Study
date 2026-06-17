package com.example.demo.Config;

import com.example.demo.Filter.MemoFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MemoFilter> memoFilter(){
        FilterRegistrationBean<MemoFilter> registrationBean =
            new FilterRegistrationBean<MemoFilter>();
        registrationBean.setFilter(new MemoFilter());
        registrationBean.addUrlPatterns("/memo/*");
        registrationBean.setOrder(1);   //실행순서
        return registrationBean;
    }
}
