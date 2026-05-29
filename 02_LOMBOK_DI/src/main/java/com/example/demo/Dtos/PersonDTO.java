package com.example.demo.Dtos;

// LOMBOK 확인 : CTRL + F12
// JUNIT TEST CASE : CTRL + SHIFT + T

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

//@Getter
//@Setter
//@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class PersonDTO {
    private String name;
    private int age;
    private String addr;
}
