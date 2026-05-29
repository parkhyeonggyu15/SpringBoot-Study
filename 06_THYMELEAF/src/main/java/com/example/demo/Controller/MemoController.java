package com.example.demo.Controller;

import com.example.demo.Dtos.MemoDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
@RequestMapping("/memo")
public class MemoController {

    @InitBinder
    public void dataBinder(WebDataBinder webDataBinder){
        log.info("MemoController's dataBinder...."+webDataBinder);
        webDataBinder.registerCustomEditor(LocalDate.class,"customData",new CustomDataEditor());
    }
    //customData 파라미터 바인딩용
    private static class CustomDataEditor extends PropertyEditorSupport {

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
           log.info("CustomDataEditor's setAsText : " + text );
            //yyyy~MM~dd  => yyyy-MM-dd

            LocalDate date = null;
            if(text.isEmpty()) {
                //1) 만약 비어서 전달된다면 현재시간을기준으로 바인딩
                date = LocalDate.now();
            }else {
                //2) yyyy~MM~dd  => yyyy-MM-dd 포매팅 변환 후 바인딩
                text = text.replaceAll("~","-");
                date = LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            setValue(date);
        }
    }


    @GetMapping("/add")
    public void memoAdd() {
        log.info("GET /memo/add...");
    }
    @PostMapping("/add")
    public void memoAddPost(@Valid MemoDTO memoDTO, BindingResult result, Model model) {
        //1. 파라미터 받기
        log.info("POST /memo/add..." + memoDTO);
//        log.info("BindingResult : " + result);

        //2. 유효성 검증
        if(result.hasErrors()){
            for(FieldError error  : result.getFieldErrors()){
                log.info("Error Field : "+error.getField()+" Error Message : "+error.getDefaultMessage());
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }

        }

        //3. 서비스 실행

        //4. 뷰로 이동(+값)

    }

}
