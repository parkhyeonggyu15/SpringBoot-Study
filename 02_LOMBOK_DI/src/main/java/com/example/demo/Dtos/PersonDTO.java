package com.example.demo.Dtos;

// LOMBOK 확인 : CTRL +F12
// JUNIT TEST CASE : CTRL + SHIFT + T

import lombok.Data;

// 확인 CTRL + F12
//@Getter
//@Setter
//@ToString
@Data
public class PersonDTO {
    private String name;
    private int age;
    private String addr;
}
