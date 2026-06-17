package com.example.demo.Listener;

import jakarta.servlet.ServletContext;
import org.springframework.cglib.proxy.Dispatcher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.support.RequestHandledEvent;
import org.springframework.web.servlet.DispatcherServlet;

public class C02RequestHandledEventListener implements ApplicationListener<RequestHandledEvent> {
    @Override
    public void onApplicationEvent(RequestHandledEvent event) {
        System.out.println("C02RequestHandledEventListener's onApplicationEvent invoke..."+event.getSource());
    }
}
