package com.example.demo.Dtos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonDTOTest {

    @Test
    public void t1(){
        PersonDTO dto = new PersonDTO();
        dto.setName("홍길동");
        dto.setAge(10);
        dto.setAddr("대구");
        System.out.println(dto);

        PersonDTO dto2 = new PersonDTO("남길동",22,"서울");
        System.out.println(dto2);

        PersonDTO dto3 = PersonDTO.builder()
                .name("서길동")
                .addr("울산")
                .build();
        System.out.println(dto3);
    }

    @Autowired
    private PersonDTO personDTO;

    @Test
    public void t2(){
        System.out.println(personDTO);


    }
}