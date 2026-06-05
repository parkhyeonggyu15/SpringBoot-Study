package com.example.demo.Controller;


import com.example.demo.Domain.Common.Daos.MemoDAO;
import com.example.demo.Domain.Common.Dtos.MemoDTO;
import com.example.demo.Domain.Common.Mapper.MemoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
@RequestMapping("/memo")
public class MemoController {

    @Autowired
    private MemoDAO memoDAO;

    @Autowired
    private MemoMapper memoMapper;


    @ExceptionHandler
    public String SQLExceptionHandler(Exception e , Model model){
        log.error("MEMO SQLEXCEPTION.." + e);
        model.addAttribute("ex",e.getMessage());
        return "memo/error";
    }

    @GetMapping("/add")
    public void memoAdd() {
        log.info("GET /memo/add...");
    }

    @PostMapping("/add")
    public String memoAddPost(@Valid MemoDTO memoDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws SQLException {
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
        long result = memoDAO.insert(memoDTO);

//        memoDTO.setCreateAt(LocalDateTime.now());
//        int result = memoMapper.insert(memoDTO);


        //4. 뷰로 이동(+값)
        redirectAttributes.addFlashAttribute("message","메모추가 성공!");
        return "redirect:/memo/list";
    }

    @GetMapping("/list")
    public void list_get(Model model) throws SQLException {
        log.info("GET /memo/list...");

        model.addAttribute("list",memoDAO.selectAll());
    }

}
