package com.example.demo.Controller;

import com.example.demo.Dtos.MemoDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/memo")
public class MemoController {

    @GetMapping("/add")
    public void memoAdd() {
        log.info("GET /memo/add...");
    }
    @PostMapping("/add")
    public void memoAddPost(@Valid MemoDTO memoDTO, BindingResult result, Model model) {
        //1. 파라미터 받기
        log.info("POST /memo/add..." + memoDTO);
//        log.info("BINDING : " + result);

        //2. 유효성 검증
        if(result.hasErrors()){
            for(FieldError error : result.getFieldErrors()){
                log.info("Error field : "+error.getField()+" Error Message : "+error.getDefaultMessage());
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }

        }

        //3. 서비스 실행

        //4. 뷰로 이동(+값)
    }

}
