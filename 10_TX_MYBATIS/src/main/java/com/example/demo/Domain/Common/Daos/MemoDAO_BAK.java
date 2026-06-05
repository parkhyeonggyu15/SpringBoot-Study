package com.example.demo.Domain.Common.Daos;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MemoDAO_BAK {
    @Autowired
    private DataSource dataSource3;
    //CRUD FUNCTION
    public int insert(MemoDTO dto) throws SQLException {
        Connection conn = dataSource3.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("insert into tbl_memo values(null,?,?,?,?)");
        pstmt.setString(1,dto.getTitle());
        pstmt.setString(2,dto.getWriter());
        pstmt.setString(3,dto.getText());
        pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        int result = pstmt.executeUpdate();
        return result;
    }
    public List<MemoDTO> selectAll() throws SQLException{
        Connection conn = dataSource3.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("select * from tbl_memo order by id desc");
        ResultSet rs = pstmt.executeQuery();
        List<MemoDTO> list = new ArrayList<>();
        MemoDTO dto = null;
        while(rs.next()) {
            dto = MemoDTO.builder()
                    .id(rs.getLong("id"))
                    .title(rs.getString("title"))
                    .writer(rs.getString("writer"))
                    .text(rs.getString("text"))
                    .createAt(rs.getTimestamp("createAt").toLocalDateTime())
                    .build();
            list.add(dto);
        }
        return list;
    }
}
