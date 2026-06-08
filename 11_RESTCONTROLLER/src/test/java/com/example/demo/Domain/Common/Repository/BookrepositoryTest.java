package com.example.demo.Domain.Common.Repository;

import com.example.demo.Domain.Common.Entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Test
    public void t1(){
        bookRepository.save(new Book(1L,"t1","p1","1111"));
    }
    @Test
    public void t2(){
        List<Book> list = bookRepository.findByBookName("t1");
        list.forEach(System.out::println);

    }
}