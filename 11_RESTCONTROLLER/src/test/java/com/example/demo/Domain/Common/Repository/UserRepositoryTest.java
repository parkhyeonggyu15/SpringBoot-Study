package com.example.demo.Domain.Common.Repository;

import com.example.demo.Domain.Common.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void t1(){
        List<User> list =
        userRepository.selectAllByRole("ROLE_USER");
        list.forEach(System.out::println);

//      List<User> list = userRepository.selectAllByRoleAndPwd("ROLE_USER","1111");
//      list.forEach(System.out::println);

//      List<User> list = userRepository.selectAllByRole_2("ROLE_ADMIN");
//      list.forEach(System.out::println);

//        List<User> list = userRepository.selectAllLikeUsername("e");
//        list.forEach(System.out::println);

    }

}