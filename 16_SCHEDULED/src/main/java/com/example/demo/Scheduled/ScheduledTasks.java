package com.example.demo.Scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {

//    @Scheduled(fixedRate = 3000)    //작업시작 주기
//    public void task1(){
//        log.info("task1...."+ System.currentTimeMillis());
//    }
//    @Scheduled(fixedDelay = 3000)   //작업종료 주기
//    public void task2(){
//        log.info("task2...."+ System.currentTimeMillis());
//    }
    @Scheduled(cron = "0 * * * * *")   // 초 분 시 일 월 요일
    public void task3(){
        log.info("task3...."+ System.currentTimeMillis());
    }
}
