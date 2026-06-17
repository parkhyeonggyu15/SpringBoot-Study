package com.example.demo.Listener;


import com.example.demo.Domain.Common.Dtos.MemoDTO;
import org.springframework.context.ApplicationEvent;

public class MemoAddEvent extends ApplicationEvent {
    private MemoDTO dto;
    public MemoAddEvent(Object source, MemoDTO dto){
        super(source);  //Application 생성자 호출
        this.dto = dto;
    }

    @Override
    public String toString() {
        return "MemoAddEvent{" +
                "dto=" + dto +
                '}';
    }
}
