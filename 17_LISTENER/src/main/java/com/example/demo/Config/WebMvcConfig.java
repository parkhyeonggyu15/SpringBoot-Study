package com.example.demo.Config;

import com.example.demo.Interceptor.MemoInterceptor;
import com.example.demo.Listener.C01CustomContextRefreshedListener;
import com.example.demo.Listener.C02RequestHandledEventListener;
import com.example.demo.Listener.MemoAddEventListener;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {


    //MULTIPART CONFIG
//    @Bean
//    public MultipartConfigElement multipartConfigElement(){
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setMaxFileSize(DataSize.ofGigabytes(1));        //파일 1개 최대(16)
//        factory.setMaxRequestSize(DataSize.ofGigabytes(1));     // 요청 전체 최대(16)
//        factory.setFileSizeThreshold(DataSize.ofGigabytes(1));  // 메모리 임계치(유사 : maxInMemorySize)
//        return factory.createMultipartConfig();
//    }
//
//    //정적자원경로 지정
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/css/**").addResourceLocations("classpath:/css/");
//        registry.addResourceHandler("/resources/js/**").addResourceLocations("classpath:/js/");
//    }


    //----------------------------------------
    // INTERCEPTOR
    //----------------------------------------
    @Autowired
    MemoInterceptor memoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memoInterceptor)
                .addPathPatterns("/memo/**")
                .excludePathPatterns("","");    //정적경로 path 등록
    }

    //-----------------------------------------
    // LISTENER
    //-----------------------------------------
    @Bean
    public C01CustomContextRefreshedListener c01CustomContextRefreshedListener(){
        return new C01CustomContextRefreshedListener();
    }
    @Bean
    public C02RequestHandledEventListener c02RequestHandledEventListener(){
        return new C02RequestHandledEventListener();
    }


    @Bean
    public MemoAddEventListener memoAddEventListener(){
        return new MemoAddEventListener();
    }

}
