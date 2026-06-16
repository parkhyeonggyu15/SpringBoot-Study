package com.example.demo.Domain.Common.Repository.ex;

import com.example.demo.Domain.Common.Entity.ex.ExReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================================
 *  [EX] Spring Data JPA Repository (스켈레톤) — 댓글
 * ============================================================
 *  [EX04] TODO: JpaRepository 를 상속하세요.
 *    public interface ExReplyRepository extends JpaRepository<ExReply, Long>
 *
 *  [EX05] TODO: "관계 기반" 쿼리 메서드를 선언하세요.
 *    - 특정 게시글(board.id) 에 달린 댓글 목록
 *      List<ExReply> findByBoard_Id(Long boardId);
 *      (연관객체의 필드 탐색은 언더스코어(_)로 board.id 를 표현)
 *  - 참고 : 같은 패키지의 LendRepository.java (@Query JOIN FETCH 예시)
 * ============================================================
 */
@Repository
public interface ExReplyRepository extends JpaRepository<ExReply, Long> {

    List<ExReply> findByBoard_Id(Long boardId);
}
