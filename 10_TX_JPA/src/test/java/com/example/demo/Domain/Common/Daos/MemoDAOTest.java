package com.example.demo.Domain.Common.Daos;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MemoDAOTest {
    @Autowired
    private MemoDAO memoDAO;

    @Test
    public void t1() throws SQLException {
        memoDAO.insert(new MemoDTO(null,"제목!","ex@ex.com","내용!",null));
    }

}