package com.example.demo.Config;

import com.example.demo.Dtos.PersonDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonConfig {

    @Bean
    public PersonDTO personBean01(){
        return PersonDTO.builder()
                .name("김범수")
                .age(50)
                .addr("인천")
                .build();
    }

    @Bean(name = "personBean")
    public PersonDTO personBean02(){
        return PersonDTO.builder()
                .name("김나영")
                .age(31)
                .addr("인천")
                .build();
    }
}
