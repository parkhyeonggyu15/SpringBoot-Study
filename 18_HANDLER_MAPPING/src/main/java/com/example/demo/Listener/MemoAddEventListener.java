package com.example.demo.Listener;

import org.springframework.context.ApplicationListener;
import org.springframework.web.context.support.RequestHandledEvent;

public class MemoAddEventListener implements ApplicationListener<MemoAddEvent> {
    @Override
    public void onApplicationEvent(MemoAddEvent event) {
        System.out.println("[LISTENER] MEMO ADD EVENT " + event);
    }
}
