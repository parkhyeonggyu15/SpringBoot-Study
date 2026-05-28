package com.example.demo.DiTest;

import com.example.demo.Component.PersonComponent;
import com.example.demo.Config.PersonConfig;
import com.example.demo.Dtos.PersonDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class DiTest {

    @Autowired
    private PersonDTO personDTO;
    @Autowired
    private PersonComponent personComponent;

    @Test
    public void t1(){
        System.out.println(personDTO);
        System.out.println(personComponent);
    }

    @Autowired
    private PersonConfig personConfig;
    @Autowired
    private PersonDTO personBean01;
    @Autowired
    private PersonDTO personBean;

    @Test
    public void t2(){
        System.out.println(personConfig);
        System.out.println(personBean01);
        System.out.println(personBean);
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void t3(){
        System.out.println(applicationContext.getBean("personBean"));
        System.out.println(applicationContext.getBean("personBean01"));
    }

//    @Test
//    public void t2(){
//
//    }
}
