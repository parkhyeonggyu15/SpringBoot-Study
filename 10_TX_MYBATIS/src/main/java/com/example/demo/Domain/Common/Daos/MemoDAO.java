package com.example.demo.Domain.Common.Daos;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import com.example.demo.Domain.Common.Mapper.MemoMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MemoDAO {
    @Autowired
    private DataSource dataSource3;

    @Autowired
    private SqlSession sqlSession;
    private String NAMESPACE="com.example.demo.Domain.Common.Mapper.MemoMapper.";

    //CRUD FUNCTION
    public long insert(MemoDTO dto) throws SQLException {
        sqlSession.insert(NAMESPACE+"insert",dto);
        return dto.getId();
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
