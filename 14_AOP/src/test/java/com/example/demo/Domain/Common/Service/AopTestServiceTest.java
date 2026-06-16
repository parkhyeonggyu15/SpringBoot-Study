package com.example.demo.Domain.Common.Service;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AopTestServiceTest {

    @Autowired
    private AopTestService aopTestService;

    @Autowired
    private MemoService memoService;

    @Test
    @Transactional
    public void t1() throws Exception {
            aopTestService.run1("HELLO1");
            aopTestService.run2("HELLO2");
            memoService.memoRegistration(MemoDTO.builder().title("t1").text("text1").writer("a@a.com").build());
    }
}