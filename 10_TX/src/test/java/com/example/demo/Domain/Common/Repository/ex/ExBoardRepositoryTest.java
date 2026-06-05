package com.example.demo.Domain.Common.Repository.ex;

import com.example.demo.Domain.Common.Entity.ex.ExBoard;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExBoardRepositoryTest {
    @Autowired
    private ExBoardRepository exBoardRepository;

    @Test
    public void t1(){
        for(int i=1;i<=10;i++){
            exBoardRepository.save(new ExBoard(null,"title"+i,"content"+i));
        }
    }


}