package com.example.demo.stock.Config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.ssl.SslProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.List;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient openDartWebClient() throws SSLException {

        // 1. OpenDART 서버가 요구할 수 있는 모든 표준 Cipher Suite를 허용 목록에 등록합니다.
        // JDK 기본 공급자(JDK) 혹은 Netty 자체 공급자(OPENSSL) 모두 대응하도록 설정합니다.
        SslContext sslContext = SslContextBuilder.forClient()
                .sslProvider(SslProvider.JDK)
                .trustManager(InsecureTrustManagerFactory.INSTANCE) // 인증서 유효성 검증 생략
                .protocols("TLSv1.2", "TLSv1.3") // OpenDART가 주로 사용하는 TLS 1.2 명시
                .ciphers(null) // null을 입력하면 JDK가 지원하는 모든 암호화 알고리즘(구형 포함)을 서버에 제안합니다.
                .build();

        // 2. HTTP 클라이언트에 SSL 컨텍스트 적용
        HttpClient httpClient = HttpClient.create()
                .secure(sslSpec -> sslSpec.sslContext(sslContext));

        // 3. WebClient 빌드 및 반환
        return WebClient.builder()
                .baseUrl("https://opendart.fss.or.kr")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
