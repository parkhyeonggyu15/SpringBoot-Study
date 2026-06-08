package com.example.demo.Domain.Common.Repository.ex;

import com.example.demo.Domain.Common.Entity.ex.ExBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ============================================================
 *  [EX] Spring Data JPA Repository (스켈레톤) — 게시글
 * ============================================================
 *  [EX03] TODO: JpaRepository 를 상속하세요.
 *    public interface ExBoardRepository extends JpaRepository<ExBoard, Long>
 *    → save / findById / findAll / delete / count 자동 제공
 *  - 참고 : 같은 패키지의 MemoRepository.java
 * ============================================================
 */
@Repository
public interface ExBoardRepository extends JpaRepository<ExBoard, Long> {
}
