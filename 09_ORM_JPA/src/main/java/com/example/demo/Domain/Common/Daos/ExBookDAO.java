package com.example.demo.Domain.Common.Daos;


import com.example.demo.Domain.Common.Dtos.BookDTO;
import com.example.demo.Domain.Common.Dtos.MemoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * [EX] DataSource + JDBC 실습용 DAO (스켈레톤) — 도서(Book) 편
 * ============================================================
 * 주제 : 등록(INSERT) / 조회(SELECT) / 수정(UPDATE) / 삭제(DELETE)  = CRUD 전체
 * <p>
 * - DB    : MySQL  jdbc:mysql://localhost:3306/testdb  (root / 1234)
 * - 테이블 : tbl_book ( id, title, author, price )
 * - 연결   : ExBookController → ExBookDao(DataSource)   ※ Service 계층 없음! 직접 연결
 * - 참고   : 같은 패키지 MemoDao.java (HikariCP dataSource3, try-with-resources 예시)
 * <p>
 * [작성 순서]
 * EX01) DataSource(dataSource3) 주입
 * EX02) insert()   : 한 건 INSERT → 영향 행 수
 * EX03) findAll()  : 전체 목록 SELECT → List<BookDTO>
 * EX04) findById() : id 로 단건 SELECT → BookDTO (없으면 null)
 * EX05) update()   : 제목/저자/가격 UPDATE → 영향 행 수
 * EX06) delete()   : id 로 DELETE → 영향 행 수
 * <p>
 * [HINT] try-with-resources 로 Connection/PreparedStatement/ResultSet 을 닫는다.
 * ResultSet → BookDTO 매핑은 rs.getLong/getString/getInt 사용.
 * ============================================================
 */
@Repository
@Slf4j
public class ExBookDAO {

    // [EX01] TODO: HikariCP 로 만든 dataSource3 빈을 주입받으세요.
    //   - @Autowired
    //   - private DataSource dataSource3;

    @Autowired
    private DataSource dataSource;

    /*
     * [EX02] 등록 (INSERT)
     * - SQL : insert into tbl_book values(?,?,?,?)
     * - dto 의 id, title, author, price 를 순서대로 바인딩한다.
     * - 반환 : 영향받은 행 수
     */
    public int insert(BookDTO dto) throws SQLException {
        log.info("ExBookDao.insert... " + dto);
        // TODO: Connection -> PreparedStatement -> setXxx -> executeUpdate()
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("insert into tbl_memo values(null,?,?,?,?)")
        ) {
            pstmt.setLong(1, dto.getId());
            pstmt.setString(2, dto.getTitle());
            pstmt.setString(3, dto.getAuthor());
            pstmt.setInt(4, dto.getPrice());
            int result = pstmt.executeUpdate();
            return result;

        }


    }


    /*
     * [EX03] 전체 목록 조회
     * - SQL : select id, title, author, price from tbl_book order by id
     * - ResultSet 을 순회하며 BookDTO 로 매핑해 List 에 담아 반환한다.
     */
    public List<BookDTO> findAll() throws SQLException {
        log.info("ExBookDao.findAll...");
        // TODO: 전체 조회 후 List<BookDTO> 반환
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("select * from tbl_book order by id");
                ResultSet rs = pstmt.executeQuery();
        ) {
            List<MemoDTO> list = new ArrayList<>();
            MemoDTO dto = null;
            while (rs.next()) {
                dto = MemoDTO.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .writer(rs.getString("author"))
                        .text(rs.getString("price"))
                        .build();
                list.add(dto);
            }
            return List.of();
        }
    }

        /*
         * [EX04] 단건 조회
         * - SQL : select id, title, author, price from tbl_book where id=?
         * - 결과가 있으면 BookDTO, 없으면 null 반환
         */
        public BookDTO findById(Long id) throws SQLException {
            log.info("ExBookDao.findById... id=" + id);
            // TODO: id 로 조회 후 BookDTO 또는 null 반환
            try (
                    Connection conn = dataSource.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement("select id, title, author, price from tbl_book where id=?");

            ) {

                pstmt.setLong(1, id);

                try (ResultSet rs = pstmt.executeQuery();) {
                    BookDTO dto = null;
                    if (rs.next()) {
                        dto = BookDTO.builder()
                                .id(rs.getLong("id"))
                                .title(rs.getString("title"))
                                .author(rs.getString("author"))
                                .price(rs.getInt("price"))
                                .build();
                    }
                    return dto;
                }



        }

    }


    /*
     * [EX05] 수정
     * - SQL : update tbl_book set title=?, author=?, price=? where id=?
     * - 반환 : 영향받은 행 수
     */
    public int update(BookDTO dto) throws SQLException {
        log.info("ExBookDao.update... " + dto);
        // TODO: UPDATE 실행 후 executeUpdate() 결과 반환
        try(
                Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("update tbl_book set title=?,author=?,price=? where id=?");
        ) {
            pstmt.setString(1, dto.getTitle());
            pstmt.setString(2, dto.getAuthor());
            pstmt.setInt(3, dto.getPrice());
            pstmt.setLong(4, dto.getId());
            int result = pstmt.executeUpdate();
            return result;
        }

    }


    /*
     * [EX06] 삭제
     * - SQL : delete from tbl_book where id=?
     * - 반환 : 영향받은 행 수
     */
    public int delete(Long id) throws SQLException {
        log.info("ExBookDao.delete... id=" + id);
        // TODO: DELETE 실행 후 executeUpdate() 결과 반환
        try(
                Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("delete from tbl_book where id=?");
                ){
            pstmt.setLong(1, id);
            int result = pstmt.executeUpdate();
            return result;

        }

    }
}



