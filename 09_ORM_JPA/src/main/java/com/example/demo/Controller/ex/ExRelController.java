package com.example.demo.Controller.ex;


import com.example.demo.Domain.Common.Entity.ex.ExBoard;
import com.example.demo.Domain.Common.Entity.ex.ExReply;

import com.example.demo.Domain.Common.Repository.ex.ExBoardRepository;
import com.example.demo.Domain.Common.Repository.ex.ExReplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================
 *  [EX] JPA 관계매핑 실습 문제 — 게시글(1) ↔ 댓글(N)
 * ============================================================
 *  ★ 구조 :  Controller → Repository → Entity   (Service 계층 없음, Repository 직접 주입)
 *  ★ 화면 :  Thymeleaf  templates/exrel/board.html  (이미 작성됨)
 *
 *  - 서버 포트 : 8097
 *
 *  [선행 준비] application.properties 의 아래 항목 주석 해제
 *     spring.datasource.url=jdbc:mysql://localhost:3306/testdb
 *     spring.datasource.username=root
 *     spring.datasource.password=1234
 *     spring.jpa.hibernate.ddl-auto=update
 *     spring.jpa.show-sql=true
 *   → 실행 시 ex_board / ex_reply 테이블 + board_id 외래키(FK) 자동 생성
 *
 *  [작성 흐름]
 *   EX01 ExBoard 엔티티 매핑        (Entity/ex/ExBoard.java)
 *   EX02 ExReply 엔티티 + @ManyToOne (Entity/ex/ExReply.java)
 *   EX03 ExBoardRepository 상속
 *   EX04 ExReplyRepository 상속
 *   EX05 관계 쿼리메서드 findByBoard_Id
 *   EX06 아래 컨트롤러 TODO (화면 목록 + 게시글/댓글 등록)
 *
 *  [화면 테스트] http://localhost:8097/exrel
 *   1) 게시글 등록 폼 → 저장
 *   2) 목록의 "댓글보기" → ?boardId=N → 그 글의 댓글 + 댓글등록 폼
 *   3) 댓글 등록 → 게시글(FK) 연결되어 저장
 * ============================================================
 */
@Controller
@Slf4j
@RequestMapping("/exrel")
public class ExRelController {

    // [EX06] Repository 직접 주입 (Service 없음)
    @Autowired
    private ExBoardRepository exBoardRepository;

    @Autowired
    private ExReplyRepository exReplyRepository;


    /*
     * [EX06] 화면 (GET /exrel?boardId=)
     *  - 게시글 전체를 model("boards") 에 담는다.
     *  - boardId 가 있으면 그 게시글(model "board")과 댓글목록(model "replies")도 담는다.
     */
    @GetMapping("/board")
    public String page(@RequestParam(required = false) Long boardId, Model model) {
        log.info("GET /exrel... boardId=" + boardId);

        model.addAttribute("boards", exBoardRepository.findAll());

         if (boardId != null) {
                     model.addAttribute("board", exBoardRepository.findById(boardId).orElse(null));
                     model.addAttribute("replies", exReplyRepository.findByBoard_Id(boardId));
         }
        return "exrel/board";
    }


    /* [EX06] 게시글 등록 (POST /exrel/board) → 목록으로 redirect */
    @PostMapping("/board")
    public String addBoard(@RequestParam String title,
                           @RequestParam String content,
                           RedirectAttributes redirectAttributes) {
        log.info("POST /exrel/board... title=" + title);

        exBoardRepository.save(ExBoard.builder().title(title).content(content).build());

        redirectAttributes.addFlashAttribute("msg", "게시글 등록!");
        return "redirect:/exrel/board";
    }


    /* [EX06] 댓글 등록 (POST /exrel/reply) - 게시글(FK) 연결 → 그 게시글 화면으로 redirect */
    @PostMapping("/reply")
    public String addReply(@RequestParam Long boardId,
                           @RequestParam String content,
                           RedirectAttributes redirectAttributes) {
        log.info("POST /exrel/reply... boardId=" + boardId);
        // TODO:
        ExBoard board = exBoardRepository.findById(boardId).orElseThrow();
        exReplyRepository.save(ExReply.builder().content(content).board(board).build());
        redirectAttributes.addFlashAttribute("msg", "댓글 등록!");
        return "redirect:/exrel/board?boardId=" + boardId;
    }
}
