package com.example.demo.Aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class LoggingAspect {

//    @Before("execution(* com.example.demo.Domain.Common.Service.AopTestService.run1(..))")
//    @Before("execution(* com.example.demo.Domain.Common.Service.AopTestService.*(..))")
//    @Before("execution(* com.example.demo.Domain.Common.Service.*.*(..))")
//    public void logginBefore(JoinPoint joinPoint){
//        log.info("[AOP] BEFORE..." + joinPoint);
//    }

//    @After("execution(* com.example.demo.Domain.Common.Service.AopTestService.run1(..))")
//    @After("execution(* com.example.demo.Domain.Common.Service.AopTestService.*(..))")
    @After("execution(* com.example.demo.Domain.Common.Service.*.*(..))")
    public void logginAfter(JoinPoint joinPoint){
        log.info("[AOP] AFTER..." + joinPoint);
    }

    @Around("execution(* com.example.demo.Domain.Common.Service.*.*(..))")
    public Object logginAround(ProceedingJoinPoint pjp) throws Throwable {
        //BEFORE 처리코드
        long start_time = System.currentTimeMillis();
        log.info("[AOP] AROUND BEFORE");

        //타겟 함수 실행
        Object returnValue = pjp.proceed();
        log.info("타겟 함수 리턴값 : " + returnValue);

        //AFTER 처리코드
        log.info("[AOP] AROUND AFTER");
        long end_time = System.currentTimeMillis();
        log.info("[AOP] 소요시간 : " + (end_time-start_time)+" ms");
        return returnValue;
    }
}
