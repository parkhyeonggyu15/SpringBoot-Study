package com.example.demo.Controller;


import com.example.demo.Domain.Common.Dtos.MemoDTO;
import com.example.demo.Domain.Common.Dtos.PageDTO;
import com.example.demo.Domain.Common.Entity.Memo;
import com.example.demo.Domain.Common.Service.MemoService;
import jakarta.validation.Valid;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/memo")
public class Memo_addRest_Controller {

    @Autowired
    private MemoService memoService;


    @ExceptionHandler
    public String SQLExceptionHandler(Exception e , Model model){
        log.error("MEMO SQLEXCEPTION.." + e);
        model.addAttribute("ex",e.getMessage());
        e.printStackTrace();
        return "memo/error";
    }

//    ----------------------------------------------
//    REST HANDLER
//    ----------------------------------------------

    @GetMapping("/rest/xhr")
    public String restIndex_xhr(){
        log.info("GET /memo/rest/xhr");
        return "memo/rest/xhr";
    }
    @GetMapping("/rest/fetch")
    public String restIndex_fetch(){
        log.info("GET /memo/rest/fetch");
        return "memo/rest/fetch";
    }
    @GetMapping("/rest/jquery")
    public String restIndex_ajax(){
        log.info("GET /memo/rest/jquery");
        return "memo/rest/jquery";
    }
    @GetMapping("/rest/axios")
    public String restIndex_axios(){
        log.info("GET /memo/rest/axios");
        return "memo/rest/axios";
    }
    @ResponseBody
    @GetMapping(value = "/rest/list" , consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity< Map<String,Object>  > rest_list_get(
            PageDTO pageDTO, Model model) throws Exception {
        log.info("GET /rest/list..."+pageDTO);
        Map<String,Object> responseMap = new HashMap<>();
        //파라미터

        //유효성

        //서비스(+페이징처리)
        Map<String,Object> r = memoService.getMemoList(pageDTO);

        responseMap.put("page",r.get("page")   );
        responseMap.put("list",r.get("list")    );
        responseMap.put("pageBlock",r.get("pageBlock")  );

        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }

    @ResponseBody
    @PostMapping(value = "/rest/add",consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity< Map<String,Object>  > memoAddPost_rest(@RequestBody @Valid MemoDTO memoDTO, BindingResult bindingResult, Model model ) throws Exception {
        Map<String,Object> responseMap = new HashMap<>();
        //1. 파라미터 받기
        log.info("POST /memo/add..." + memoDTO);
//        log.info("BindingResult : " + result);

        //2. 유효성 검증
        if(bindingResult.hasErrors()){
            for(FieldError error  : bindingResult.getFieldErrors()){
                log.info("Error Field : "+error.getField()+" Error Message : "+error.getDefaultMessage());
                responseMap.put(error.getField(),error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
        }

        //3. 서비스 실행
        boolean isRegistration = memoService.memoRegistration(memoDTO);

        //4. 뷰로 이동(+값)
        responseMap.put("message","메모추가 성공!");
        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }

//    ----------------------------------------------
//    REST HANDLER
//    ----------------------------------------------


    @GetMapping("/add")
    public void memoAdd() {
        log.info("GET /memo/add...");
    }

    @PostMapping("/add")
    public String memoAddPost(@Valid MemoDTO memoDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws Exception {
        //1. 파라미터 받기
        log.info("POST /memo/add..." + memoDTO);
//        log.info("BindingResult : " + result);

        //2. 유효성 검증
        if(bindingResult.hasErrors()){
            for(FieldError error  : bindingResult.getFieldErrors()){
                log.info("Error Field : "+error.getField()+" Error Message : "+error.getDefaultMessage());
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            return "memo/add";
        }

        //3. 서비스 실행
        boolean isRegistration = memoService.memoRegistration(memoDTO);

        //4. 뷰로 이동(+값)
        redirectAttributes.addFlashAttribute("message","메모추가 성공!");
        return "redirect:/memo/list";
    }

    @ResponseBody
    @PutMapping(value = "/rest/update",consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity< Map<String,Object>  > memoUpdatePut_rest(@RequestBody @Valid MemoDTO memoDTO, BindingResult bindingResult) throws Exception {
        Map<String,Object> responseMap = new HashMap<>();
        //1. 파라미터 받기
        log.info("POST /memo/rest/update..." + memoDTO);
//        log.info("BindingResult : " + result);

        //2. 유효성 검증
        if(bindingResult.hasErrors()){
            for(FieldError error  : bindingResult.getFieldErrors()){
                log.info("Error Field : "+error.getField()+" Error Message : "+error.getDefaultMessage());
                responseMap.put(error.getField(),error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
        }
        //3. 서비스 실행
        boolean isRegistration = memoService.updateMemo(memoDTO);

        //4. 뷰로 이동(+값)
        responseMap.put("message","메모수정 성공!");
        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }

    @ResponseBody
    @DeleteMapping(value = "/rest/delete",consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity< Map<String,Object>  > memoDelete_rest(@RequestBody MemoDTO dto) throws Exception {
        Map<String,Object> responseMap = new HashMap<>();
        //1. 파라미터 받기
        log.info("POST /memo/rest/delete..." + dto.getId());
//        log.info("BindingResult : " + result);


        //3. 서비스 실행
        boolean isRegistration = memoService.removeMemo(dto.getId());

        //4. 뷰로 이동(+값)
        responseMap.put("message","메모삭제 성공!");
        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }

    @GetMapping("/list")
    public void list_get(
            PageDTO pageDTO, Model model) throws Exception {
        log.info("GET /memo/list..."+pageDTO);
        //파라미터

        //유효성

        //서비스(+페이징처리)
        Map<String,Object> r = memoService.getMemoList(pageDTO);

        model.addAttribute("page",r.get("page"));
        model.addAttribute("list",r.get("list"));
        model.addAttribute("pageBlock",r.get("pageBlock"));

//        model.addAttribute("list",memoDAO.selectAll());
//        model.addAttribute("list",memoRepository.findAll());

        //뷰
    }

    @GetMapping("/update")
    public void memo_update(Long id,Model model) throws Exception {
        log.info("GET /memo/update...." + id);

        //
        MemoDTO dto = memoService.getMemo(id);

        model.addAttribute("dto",dto);

    }
    @PostMapping("/update")
    public String memo_update_post(MemoDTO dto,Model model,RedirectAttributes redirectAttributes) throws Exception {
        log.info("GET /memo/update...." + dto);
        //1 파라미터
        //2 유효성
        //3 서비스(수정)
//        int result = memoDAO.update(dto);

        boolean isUpdated = memoService.updateMemo(dto);
        if(isUpdated)
            redirectAttributes.addFlashAttribute("message",dto.getId() + " 업데이트 성공!");
        //4 뷰로이동(+값 , +메시지)

        return "redirect:/memo/list";
    }

    @GetMapping("/delete")
    public String memo_delete(Long id,RedirectAttributes redirectAttributes) throws Exception {
        log.info("GET /memo/delete...." + id);

//        int result = memoDAO.delete(id);
//        memoRepository.deleteById(id);

        boolean isRemoved = memoService.removeMemo(id);

        if(isRemoved)
            redirectAttributes.addFlashAttribute("message",id + " 삭제 성공!");

        return "redirect:/memo/list";
    }



}
