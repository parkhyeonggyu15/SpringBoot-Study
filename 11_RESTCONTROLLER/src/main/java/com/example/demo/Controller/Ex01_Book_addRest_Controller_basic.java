//package com.example.demo.Controller;
//
//import com.example.demo.Domain.Common.Dtos.Ex01_BookDTO;
//import com.example.demo.Domain.Common.Dtos.PageDTO;
//// import com.example.demo.Domain.Common.Service.Ex01_BookService;  // TODO-5 완성 후 주석 해제
//import com.example.demo.Domain.Common.Service.Ex01_BookService;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * [Ex01] 도서 REST 실습 - RestController  (학생 작성용 / 문제)
// *  Memo_addRest_Controller 의 REST 핸들러 구조를 그대로 본떠 완성하세요.
// */
//@Controller
//@Slf4j
//@RequestMapping("/book")
//public class Ex01_Book_addRest_Controller_basic {
//
//    // TODO: 위 import 와 함께 Ex01_BookService 를 @Autowired 로 주입하세요.
//    @Autowired
//    private Ex01_BookService bookService;
//    // TODO-11: @ExceptionHandler 작성 — 예외 로그 남기고 model 에 메시지 담아 "book/error" 반환
//    @ExceptionHandler
//    public String ExceptionHandler (Exception e, Model model){
//        model.addAttribute("e",e.getMessage());
//        return "book/error";
//    }
//    // ---------------------------------------------- REST HANDLER ----------------------------------------------
//
//    // TODO-12: 4가지 통신방식 화면 진입용 GET 핸들러를 각각 만드세요 (반환값 = 뷰 이름)
//    //   - GET /book/rest/xhr   → "book/rest/xhr"
//    //   - GET /book/rest/fetch → "book/rest/fetch"
//    //   - GET /book/rest/ajax  → "book/rest/ajax"
//    //   - GET /book/rest/axios → "book/rest/axios"
//    @GetMapping("/rest/xhr")
//    public String restIndex_xhr(){
//        log.info("GET /book/rest/xhr");
//        return "book/rest/xhr";
//    }
//    @GetMapping("/rest/fetch")
//    public String restIndex_fetch(){
//        log.info("GET /book/rest/fetch");
//        return "book/rest/fetch";
//    }
//    @GetMapping("/ajax/")
//    public String ajax(){
//        log.info("GET /book/rest/ajax");
//        return "book/rest/ajax";
//    }
//    @GetMapping("/rest/axios")
//    public String restIndex_axios(){
//        log.info("GET /book/rest/axios");
//        return "book/rest/axios";
//    }
//
//    // TODO-13: @ResponseBody GET /book/rest/list (produces JSON)
//    //   - bookService.getBookList(pageDTO) 결과의 page/list/pageBlock 를 Map 에 담아 OK 응답
//    //   - 파라미터: PageDTO pageDTO
//    @ResponseBody
//    @GetMapping(value = "/book/rest/list" , consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity< Map<String,Object>  > rest_list_get(
//            PageDTO pageDTO, Model model) throws Exception {
//        log.info("GET /rest/list..."+pageDTO);
//        Map<String,Object> responseMap = new HashMap<>();
//        //파라미터
//
//        //유효성
//
//        //서비스(+페이징처리)
//        Map<String,Object> r = bookService.getBookList(pageDTO);
//
//        responseMap.put("page",r.get("page")   );
//        responseMap.put("list",r.get("list")    );
//        responseMap.put("pageBlock",r.get("pageBlock")  );
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
//    }
//
//    // TODO-14: @ResponseBody POST /book/rest/add (consumes/produces JSON)
//    //   - @RequestBody @Valid Ex01_BookDTO dto, BindingResult bindingResult
//    //   - hasErrors() 면 필드별 에러를 Map 에 담아 400(BAD_REQUEST) 반환
//    //   - 정상이면 bookService.bookRegistration(dto) 후 {"message":"도서추가 성공!"}
//    @ResponseBody
//    @PostMapping(value = "/book/rest/add",consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity< Map<String,Object>  > bookAddPost_rest(@RequestBody @Valid Ex01_BookDTO dto, BindingResult bindingResult) throws Exception {
//        Map<String,Object> responseMap = new HashMap<>();
//        //1. 파라미터 받기
//        log.info("POST /book/rest/add..." + dto);
//
//        //2. 유효성 검증
//        if(bindingResult.hasErrors()){
//            for(FieldError error  : bindingResult.getFieldErrors()){
//                log.info("Error Field : "+error.getField()+" Error Message : "+error.getDefaultMessage());
//                responseMap.put(error.getField(),error.getDefaultMessage());
//            }
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
//        }
//
//        //3. 서비스 실행
//        boolean isRegistration = bookService.bookRegistration(dto);
//
//        //4. 뷰로 이동(+값)
//        responseMap.put("message","도서추가 성공!");
//        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
//    }
//
//    // TODO-15: @ResponseBody PUT /book/rest/update — add 와 동일 패턴, {"message":"도서수정 성공!"}
//    @ResponseBody
//    @PutMapping(value = "/book/rest/update",consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity< Map<String,Object>  > bookUpdatePut_rest(@RequestBody @Valid Ex01_BookDTO dto, BindingResult bindingResult) throws Exception {
//        Map<String,Object> responseMap = new HashMap<>();
//        //1. 파라미터 받기
//        log.info("POST /book/rest/update..." + dto);
//
//        //2. 유효성 검증
//        if(bindingResult.hasErrors()){
//            for(FieldError error  : bindingResult.getFieldErrors()){
//                log.info("Error Field : "+error.getField()+" Error Message : "+error.getDefaultMessage());
//                responseMap.put(error.getField(),error.getDefaultMessage());
//            }
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
//        }
//        //3. 서비스 실행
//        boolean isRegistration = bookService.updateBook(dto);
//
//        //4. 뷰로 이동(+값)
//        responseMap.put("message","도서수정 성공!");
//        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
//    }
//
//    // TODO-16: @ResponseBody DELETE /book/rest/delete
//    //   - @RequestBody Ex01_BookDTO dto 로 bookCode 받아 removeBook, {"message":"도서삭제 성공!"}
//    @ResponseBody
//    @DeleteMapping(value = "/book/rest/delete",consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity< Map<String,Object>  > bookDelete_rest(@RequestBody Ex01_BookDTO dto) throws Exception {
//        Map<String,Object> responseMap = new HashMap<>();
//        //1. 파라미터 받기
//        log.info("POST /book/rest/delete..." + dto.getBookCode());
////        log.info("BindingResult : " + result);
//
//
//        //3. 서비스 실행
//        boolean isRegistration = bookService.removeBook(dto.getBookCode());
//
//        //4. 뷰로 이동(+값)
//        responseMap.put("message","도서삭제 성공!");
//        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
//    }
//
//}
