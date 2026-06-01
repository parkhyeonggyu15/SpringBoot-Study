package com.example.demo.Controller;


import com.example.demo.Domain.Common.Daos.ExBookDAO;
import com.example.demo.Domain.Common.Dtos.BookDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================
 *  [EX] DataSource / JDBC 실습 문제 — 도서(Book) 편  (CRUD 전체)
 * ============================================================
 *  ★ 이 문제는 Service 계층 없이 Controller 가 ExBookDao 를 "직접" 호출한다.
 *     흐름 :  ExBookController  →  ExBookDao(DataSource)
 *
 *  - DB : MySQL testdb, 테이블 tbl_book(id, title, author, price)
 *  - 연동 화면(이미 작성됨, Thymeleaf) :
 *        templates/exbook/list.html     (목록 + 추가/수정링크 + 삭제버튼)
 *        templates/exbook/add.html      (등록 폼)
 *        templates/exbook/update.html   (수정 폼)
 *
 *  [선행 준비]
 *    create table tbl_book(
 *       id bigint primary key,
 *       title  varchar(255),
 *       author varchar(100),
 *       price  int
 *    );
 *
 *  [작성 순서]
 *    EX07) ExBookDao 직접 주입
 *    EX08) GET  /exbook/list    : 전체 목록 → model("bookList")
 *    EX09) GET  /exbook/add     : 등록 폼 보여주기
 *    EX10) POST /exbook/add     : 등록 후 목록으로 redirect
 *    EX11) GET  /exbook/update  : id 단건 조회 → model("book") (수정 폼 채우기)
 *    EX12) POST /exbook/update  : 수정 후 목록으로 redirect
 *    EX13) POST /exbook/delete  : 삭제 후 목록으로 redirect
 * ============================================================
 */
@Controller
@Slf4j
@RequestMapping("/exbook")
public class ExBookController {

    // [EX07] TODO: ExBookDao 를 직접 주입하세요. (Service 거치지 않음)
    //   - @Autowired
    //   - private ExBookDao exBookDao;

    @Autowired
    private ExBookDAO exBookDAO;

    /*
     * [EX08] 목록 조회 (SELECT)
     * - exBookDao.findAll() 결과를 model 에 "bookList" 로 담는다.
     */
    @GetMapping("/list")
    public String list(Model model) throws Exception {
        log.info("GET /exbook/list...");
        // TODO: model.addAttribute("bookList", exBookDao.findAll());
        model.addAttribute("bookList",exBookDAO.findAll());
        return "exbook/list";
    }


    /*
     * [EX09] 등록 폼 (GET)
     * - 빈 폼(templates/exbook/add.html)을 보여주기만 한다. (DB 작업 없음)
     */
    @GetMapping("/add")
    public String addForm() throws Exception {
        log.info("GET /exbook/add...");
        // TODO: 등록 폼 뷰 이름 반환
        return "exbook/add";
    }


    /*
     * [EX10] 등록 처리 (INSERT)
     * - 폼 데이터를 BookDTO 로 받아 exBookDao.insert(dto) 호출.
     * - 성공 시 RedirectAttributes 로 "message" 플래시 속성 추가.
     * - 처리 후 redirect:/exbook/list
     */
    @PostMapping("/add")
    public String add(BookDTO dto, RedirectAttributes redirectAttributes) throws Exception {
        log.info("POST /exbook/add... " + dto);
        // TODO 1: boolean ok = exBookDao.insert(dto) > 0;
        boolean ok = exBookDAO.insert(dto) > 0;
        // TODO 2: if(ok) redirectAttributes.addFlashAttribute("message","도서등록완료!");
        if(ok){
            redirectAttributes.addFlashAttribute("message","도서등록완료!");
        }

        return "redirect:/exbook/list";
    }


    /*
     * [EX11] 수정 폼 (단건 SELECT 후 폼에 채워서 보여주기)
     * - exBookDao.findById(id) 결과를 model 에 "book" 으로 담는다.
     */
    @GetMapping("/update")
    public String updateForm(@RequestParam Long id, Model model) throws Exception {
        log.info("GET /exbook/update... id=" + id);
        // TODO: model.addAttribute("book", exBookDao.findById(id));
        model.addAttribute("book",exBookDAO.findById(id));
        return "exbook/update";
    }


    /*
     * [EX12] 수정 처리 (UPDATE)
     * - 폼 데이터를 BookDTO 로 받아 exBookDao.update(dto) 호출.
     * - 성공 시 RedirectAttributes 로 "message" 플래시 속성 추가.
     * - 처리 후 redirect:/exbook/list
     */
    @PostMapping("/update")
    public String update(BookDTO dto, RedirectAttributes redirectAttributes) throws Exception {
        log.info("POST /exbook/update... " + dto);
        // TODO 1: boolean ok = exBookDao.update(dto) > 0;
        boolean ok = exBookDAO.update(dto)>0;
        // TODO 2: if(ok) redirectAttributes.addFlashAttribute("message","도서수정완료!");
        if(ok){
            redirectAttributes.addFlashAttribute("message","도서수정완료!");
        }

        return "redirect:/exbook/list";
    }


    /*
     * [EX13] 삭제 처리 (DELETE)
     * - id 를 받아 exBookDao.delete(id) 호출.
     * - 성공 시 "message" 플래시 속성 추가 후 redirect:/exbook/list
     */
    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) throws Exception {
        log.info("POST /exbook/delete... id=" + id);
        // TODO 1: boolean ok = exBookDao.delete(id) > 0;
        boolean ok = exBookDAO.delete(id) > 0;
        // TODO 2: if(ok) redirectAttributes.addFlashAttribute("message","도서삭제완료!");
        if(ok) redirectAttributes.addFlashAttribute("message","도서삭제완료!");
        return "redirect:/exbook/list";
    }
}
