package com.example.demo.Domain.Common.Entity;

import com.example.demo.Domain.Common.Repository.BookRepository;
import com.example.demo.Domain.Common.Repository.LendRepository;
import com.example.demo.Domain.Common.Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class LendTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LendRepository lendRepository;


    @Test
    public void t1(){
        //INSERT
        User user = userRepository.findById("user1").get();
        Book book = bookRepository.findById(1111L).get();
        Lend lend = Lend.builder()
                .user(user)
                .book(book)
                .build();
        lendRepository.save(lend);
    }
    @Test
    public void t2(){
        //유저이름으로 대여 내역 조회
//        List<Lend> list = lendRepository.findAllLendsByUser("user1");
//        list.forEach(System.out::println);
        //도서이름으로 내역 내역 조회
        List<Lend> list = lendRepository.findAllLendsByBook("JAVA의정석");
        list.forEach(System.out::println);
    }

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    public void t3(){
        em.flush();
        em.clear();
        System.out.println("1 start----------------");
        Lend lend = lendRepository.findById(1L).get();
        System.out.println("1 end----------------");

        System.out.println("2 start----------------");
        System.out.println(lend.getUser().getUsername());
        System.out.println("2 start----------------");

        System.out.println("3 start----------------");
        System.out.println(lend.getBook().getBookName());
        System.out.println("3 start----------------");

    }

}