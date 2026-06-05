package com.example.demo.Domain.Common.Daos;


import com.example.demo.Domain.Common.Dtos.BookDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * ============================================================
 *  [EX] DataSource + JDBC 실습용 DAO (스켈레톤) — 도서(Book) 편
 * ============================================================
 *  주제 : 등록(INSERT) / 조회(SELECT) / 수정(UPDATE) / 삭제(DELETE)  = CRUD 전체
 *
 *  - DB    : MySQL  jdbc:mysql://localhost:3306/testdb  (root / 1234)
 *  - 테이블 : tbl_book ( id, title, author, price )
 *  - 연결   : ExBookController → ExBookDao(DataSource)   ※ Service 계층 없음! 직접 연결
 *  - 참고   : 같은 패키지 MemoDao.java (HikariCP dataSource3, try-with-resources 예시)
 *
 *  [작성 순서]
 *   EX01) DataSource(dataSource3) 주입
 *   EX02) insert()   : 한 건 INSERT → 영향 행 수
 *   EX03) findAll()  : 전체 목록 SELECT → List<BookDTO>
 *   EX04) findById() : id 로 단건 SELECT → BookDTO (없으면 null)
 *   EX05) update()   : 제목/저자/가격 UPDATE → 영향 행 수
 *   EX06) delete()   : id 로 DELETE → 영향 행 수
 *
 *  [HINT] try-with-resources 로 Connection/PreparedStatement/ResultSet 을 닫는다.
 *         ResultSet → BookDTO 매핑은 rs.getLong/getString/getInt 사용.
 * ============================================================
 */
@Repository
@Slf4j
public class ExBookDAO {

    // [EX01] TODO: HikariCP 로 만든 dataSource3 빈을 주입받으세요.
    //   - @Autowired
    //   - private DataSource dataSource3;


    /*
     * [EX02] 등록 (INSERT)
     * - SQL : insert into tbl_book values(?,?,?,?)
     * - dto 의 id, title, author, price 를 순서대로 바인딩한다.
     * - 반환 : 영향받은 행 수
     */
    public int insert(BookDTO dto) throws SQLException {
        log.info("ExBookDao.insert... " + dto);
        // TODO: Connection -> PreparedStatement -> setXxx -> executeUpdate()
        return 0;
    }


    /*
     * [EX03] 전체 목록 조회
     * - SQL : select id, title, author, price from tbl_book order by id
     * - ResultSet 을 순회하며 BookDTO 로 매핑해 List 에 담아 반환한다.
     */
    public List<BookDTO> findAll() throws SQLException {
        log.info("ExBookDao.findAll...");
        // TODO: 전체 조회 후 List<BookDTO> 반환
        return List.of();
    }


    /*
     * [EX04] 단건 조회
     * - SQL : select id, title, author, price from tbl_book where id=?
     * - 결과가 있으면 BookDTO, 없으면 null 반환
     */
    public BookDTO findById(Long id) throws SQLException {
        log.info("ExBookDao.findById... id=" + id);
        // TODO: id 로 조회 후 BookDTO 또는 null 반환
        return null;
    }


    /*
     * [EX05] 수정
     * - SQL : update tbl_book set title=?, author=?, price=? where id=?
     * - 반환 : 영향받은 행 수
     */
    public int update(BookDTO dto) throws SQLException {
        log.info("ExBookDao.update... " + dto);
        // TODO: UPDATE 실행 후 executeUpdate() 결과 반환
        return 0;
    }


    /*
     * [EX06] 삭제
     * - SQL : delete from tbl_book where id=?
     * - 반환 : 영향받은 행 수
     */
    public int delete(Long id) throws SQLException {
        log.info("ExBookDao.delete... id=" + id);
        // TODO: DELETE 실행 후 executeUpdate() 결과 반환
        return 0;
    }
}
