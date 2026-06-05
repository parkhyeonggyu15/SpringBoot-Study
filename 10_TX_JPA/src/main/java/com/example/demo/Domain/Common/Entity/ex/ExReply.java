package com.example.demo.Domain.Common.Entity.ex;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================
 *  [EX] JPA 관계매핑 실습 (스켈레톤) — 댓글(자식, "N" 쪽)
 * ============================================================
 *  - 여러 댓글(ExReply) 이 하나의 게시글(ExBoard) 에 속한다.  (N : 1)
 *  - 참고 : Entity 패키지의 Lend.java (@ManyToOne + @JoinColumn 예시)
 *
 *  [EX02] TODO - 아래 매핑 애너테이션을 직접 작성하세요.
 *   1) 클래스 위 : @Entity, @Table(name="ex_reply")
 *   2) id        : @Id, @GeneratedValue(strategy = GenerationType.IDENTITY)
 *   3) content   : @Column(length=1000, nullable=false)
 *   4) board     : ★관계매핑★
 *        @ManyToOne
 *        @JoinColumn(name="board_id")   // ex_reply 테이블에 board_id FK 컬럼 생성
 *        private ExBoard board;
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="ex_reply")
public class ExReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=1000, nullable=false)
    private String content;

    // [관계매핑] 여러 댓글(N) -> 하나의 게시글(1)
    @ManyToOne
    @JoinColumn(
            name="board_id",
            foreignKey = @ForeignKey(
                    name="FK_REPLY_BOARD",
                    foreignKeyDefinition = "FOREIGN KEY (board_id) REFERENCES ex_board(id) ON DELETE CASCADE ON UPDATE CASCADE"
            )
    )
    private ExBoard board;
}
