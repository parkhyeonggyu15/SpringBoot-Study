package com.example.demo.Controller;


import com.example.demo.Domain.Common.Daos.MemoDAO;
import com.example.demo.Domain.Common.Dtos.MemoDTO;
import com.example.demo.Domain.Common.Dtos.PageBlock;
import com.example.demo.Domain.Common.Dtos.PageDTO;
import com.example.demo.Domain.Common.Entity.Memo;
import com.example.demo.Domain.Common.Repository.MemoRepository;
import com.example.demo.Domain.Common.Service.MemoService;
import com.example.demo.Domain.Common.Service.MemoServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/memo")
public class MemoController {

    @Autowired
    private MemoService memoService;


    @ExceptionHandler
    public String SQLExceptionHandler(Exception e , Model model){
        log.error("MEMO SQLEXCEPTION.." + e);
        model.addAttribute("ex",e.getMessage());
        e.printStackTrace();
        return "memo/error";
    }

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
