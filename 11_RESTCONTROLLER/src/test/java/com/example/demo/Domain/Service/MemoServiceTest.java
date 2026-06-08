package com.example.demo.Domain.Service;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import com.example.demo.Domain.Common.Service.MemoServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemoServiceTest {
    @Autowired
    private MemoServiceImpl memoServiceImpl;

    @Test
    public void t1() throws Exception {
        memoServiceImpl.memoRegistration(new MemoDTO(null,"tt","tt","tt",null));
    }
}