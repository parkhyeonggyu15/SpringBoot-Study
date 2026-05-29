package com.example.demo.Component;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PersonComponent {
    private String name;
    private int age;
    private String addr;
    PersonComponent(){
        this.name = "티모";
        this.age = 100;
        this.addr = "창원";
    }
}
