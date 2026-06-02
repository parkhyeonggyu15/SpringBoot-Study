package com.example.demo.Domain.Common.Repository;

import com.example.demo.Domain.Common.Entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoRepositoryTest {

    @Autowired
    private MemoRepository memoRepository;

    @Test
    public void t1(){
        Memo memo = new Memo(null,"text3","c@c.com", LocalDateTime.now());
        System.out.println(memo);
        memoRepository.save(memo);
        System.out.println(memo);
    }
    @Test
    public void t2(){
        Memo memo = new Memo(3L,"text1!!!","a!!!@a.com", LocalDateTime.now());
        System.out.println(memo);
        memoRepository.save(memo);
        System.out.println(memo);
    }
    @Test
    public void t3(){
        memoRepository.deleteById(1L);
    }
    @Test
    public void t4(){
        Optional<Memo> memoOp = memoRepository.findById(3L);
        if(memoOp.isPresent())
            System.out.println(memoOp.get());
    }
    @Test
    public void t5(){
        List<Memo> list = memoRepository.findAll();
        list.forEach
                (System.out::println);
    }
}