package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception{

        //CSRF 비활성화(비활성화하지 않으면 LOGOUT 요청은 기본적으로 POST방식을 따른다)
        http.csrf((config)->{config.disable();});

        //권한처리
        http.authorizeHttpRequests((auth)->{
            auth.requestMatchers("/","/join","/login").permitAll();
            //
            auth.requestMatchers("/user").hasAnyRole("USER","ADMIN");
            auth.requestMatchers("/manager").hasAnyRole("MANAGER");
            auth.requestMatchers("/admin").hasAnyRole("ADMIN");

            auth.anyRequest().authenticated();
        });

        //로그인
        http.formLogin((login)->{
            login.permitAll();
            login.loginPage("/login");
        });

        //로그아웃
        http.logout((logout)->{
            logout.permitAll();
        });

        return http.build();
    }


    // 임시계정 생성
//    @Bean
//    UserDetailsService users(){
//        UserDetails user = User.withUsername("user")
//                .password("{noop}1234") //비밀번호 인코딩 없음 (실습용)
//                .roles("USER")          //ROLE_USER
//                .build();
//
//        UserDetails manager = User.withUsername("manager")
//                .password("{noop}1234")
//                .roles("MANAGER")       //ROLE_MANAGER
//                .build();
//
//        UserDetails admin = User.withUsername("admin")
//                .password("{noop}1234")
//                .roles("ADMIN")         //ROLE_ADMIN
//                .build();
//
//        return new InMemoryUserDetailsManager(user, manager, admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
