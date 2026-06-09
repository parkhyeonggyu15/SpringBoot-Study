package com.example.demo.Controller;

import com.example.demo.Domain.Common.Dtos.Ex01_BookDTO;
import com.example.demo.Domain.Common.Dtos.PageDTO;
// import com.example.demo.Domain.Common.Service.Ex01_BookService;  // TODO-5 완성 후 주석 해제
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * [Ex01] 도서 REST 실습 - RestController  (학생 작성용 / 문제)
 *  Memo_addRest_Controller 의 REST 핸들러 구조를 그대로 본떠 완성하세요.
 */
@Controller
@Slf4j
@RequestMapping("/book")
public class Ex01_Book_addRest_Controller {

    // TODO: 위 import 와 함께 Ex01_BookService 를 @Autowired 로 주입하세요.

    // TODO-11: @ExceptionHandler 작성 — 예외 로그 남기고 model 에 메시지 담아 "book/error" 반환

    // ---------------------------------------------- REST HANDLER ----------------------------------------------

    // TODO-12: 4가지 통신방식 화면 진입용 GET 핸들러를 각각 만드세요 (반환값 = 뷰 이름)
    //   - GET /book/rest/xhr   → "book/rest/xhr"
    //   - GET /book/rest/fetch → "book/rest/fetch"
    //   - GET /book/rest/ajax  → "book/rest/ajax"
    //   - GET /book/rest/axios → "book/rest/axios"

    // TODO-13: @ResponseBody GET /book/rest/list (produces JSON)
    //   - bookService.getBookList(pageDTO) 결과의 page/list/pageBlock 를 Map 에 담아 OK 응답
    //   - 파라미터: PageDTO pageDTO

    // TODO-14: @ResponseBody POST /book/rest/add (consumes/produces JSON)
    //   - @RequestBody @Valid Ex01_BookDTO dto, BindingResult bindingResult
    //   - hasErrors() 면 필드별 에러를 Map 에 담아 400(BAD_REQUEST) 반환
    //   - 정상이면 bookService.bookRegistration(dto) 후 {"message":"도서추가 성공!"}

    // TODO-15: @ResponseBody PUT /book/rest/update — add 와 동일 패턴, {"message":"도서수정 성공!"}

    // TODO-16: @ResponseBody DELETE /book/rest/delete
    //   - @RequestBody Ex01_BookDTO dto 로 bookCode 받아 removeBook, {"message":"도서삭제 성공!"}

}
