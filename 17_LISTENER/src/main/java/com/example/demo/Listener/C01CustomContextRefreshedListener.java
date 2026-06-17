package com.example.demo.Listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class C01CustomContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent>
{
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("C01CustomContextRefreshedListener's onApplicationEvent invoke..."+event);
    }
}
