package com.example.demo.Domain.Common.Repository;

import com.example.demo.Domain.Common.Entity.Book;
import org.springframework.stereotype.Repository;

/**
 * [Ex01] 도서 REST 실습 - BookRepository  (학생 작성용 / 문제)
 */
@Repository
public interface Ex01_BookRepository /* TODO-4: extends JpaRepository<Book, Long> 추가 */ {
    // 기본 CRUD/페이징은 JpaRepository 가 제공하므로 본문은 비워둔다.
}
