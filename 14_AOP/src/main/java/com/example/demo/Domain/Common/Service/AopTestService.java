package com.example.demo.Domain.Common.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AopTestService {

    public String run1(String param){
        log.info("[AopTestService] run1 invoke...!" );
        return "param : " +param;
    }

    public String run2(String param){
        log.info("[AopTestService] run2 invoke...!");
        return "param : "+ param;
    }
}
