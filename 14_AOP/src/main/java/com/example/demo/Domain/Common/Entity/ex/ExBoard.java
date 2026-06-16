package com.example.demo.Domain.Common.Entity.ex;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================
 *  [EX] JPA 관계매핑 실습 (스켈레톤) — 게시글(부모, "1" 쪽)
 * ============================================================
 *  - 한 개의 게시글(ExBoard) 에 여러 댓글(ExReply) 이 달린다.  (1 : N)
 *  - 참고 : Entity 패키지의 Book.java / User.java (기본 엔티티 매핑 예시)
 *
 *  [EX01] TODO - 아래 매핑 애너테이션을 직접 작성하세요.
 *   1) 클래스 위 : @Entity, @Table(name="ex_board")
 *   2) id        : @Id, @GeneratedValue(strategy = GenerationType.IDENTITY)
 *   3) title     : @Column(length=200, nullable=false)
 *   4) content   : @Column(length=2000)
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="ex_board")
public class ExBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=200, nullable=false)
    private String title;

    @Column(length=2000)
    private String content;
}
