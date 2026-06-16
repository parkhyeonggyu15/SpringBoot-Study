package com.example.demo.Config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DataSourceConfigTest {

//    @Autowired
//    private DataSource dataSource;
//
//
//    @Test
//    public void t1() throws SQLException {
//        assertNotNull(dataSource);
//        System.out.println(dataSource);
//
//        Connection conn = dataSource.getConnection();
//        PreparedStatement pstmt = conn.prepareStatement("insert into tbl_memo values(?,?,?,?,?)");
//        pstmt.setLong(1,2L);
//        pstmt.setString(2,"제목1");
//        pstmt.setString(3,"example@example.com");
//        pstmt.setString(4,"내용~~~1");
//        pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
//        int result = pstmt.executeUpdate();
//    }
    @Autowired
    private DataSource dataSource;

    @Test
    public void t2() throws SQLException {
        assertNotNull(dataSource);
        System.out.println(dataSource);

        Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("insert into tbl_memo values(?,?,?,?,?)");
        pstmt.setLong(1,3L);
        pstmt.setString(2,"제목1");
        pstmt.setString(3,"example@example.com");
        pstmt.setString(4,"내용~~~1");
        pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        int result = pstmt.executeUpdate();
    }

    @Autowired
    private DataSource dataSource3;

    @Test
    public void t3() throws SQLException {
        assertNotNull(dataSource3);
        System.out.println(dataSource3);

        Connection conn = dataSource3.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("insert into tbl_memo values(?,?,?,?,?)");
        pstmt.setLong(1,4L);
        pstmt.setString(2,"제목1");
        pstmt.setString(3,"example@example.com");
        pstmt.setString(4,"내용~~~1");
        pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        int result = pstmt.executeUpdate();
    }
}
