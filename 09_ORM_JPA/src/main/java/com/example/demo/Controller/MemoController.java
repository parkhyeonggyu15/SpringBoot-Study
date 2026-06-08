package com.example.demo.Controller;


import com.example.demo.Domain.Common.Daos.MemoDAO;
import com.example.demo.Domain.Common.Dtos.MemoDTO;
import com.example.demo.Domain.Common.Dtos.PageBlock;
import com.example.demo.Domain.Common.Dtos.PageDTO;
import com.example.demo.Domain.Common.Entity.Memo;
import com.example.demo.Domain.Common.Repository.MemoRepository;
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
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/memo")
public class MemoController {

    @Autowired
    private MemoDAO memoDAO;

    @Autowired
    private MemoRepository memoRepository;


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
//        int result = memoDAO.insert(memoDTO);
        Memo memo = Memo.builder()
                .id(memoDTO.getId())
                .text(memoDTO.getText())
                .title(memoDTO.getTitle())
                .writer(memoDTO.getWriter())
                .createAt(LocalDateTime.now())
                .build();
        memoRepository.save(memo);

        //4. 뷰로 이동(+값)
        redirectAttributes.addFlashAttribute("message","메모추가 성공!");
        return "redirect:/memo/list";
    }

    @GetMapping("/list")
    public void list_get(
            PageDTO pageDTO, Model model) throws SQLException {
        log.info("GET /memo/list..."+pageDTO);
        //파라미터

        //유효성

        //서비스(+페이징처리)
        int pageNo = 0;     //현재 pageNo
        int amount = 10;    //한페이지 표시할 게시물 건수
        if(pageDTO.getPageNo()!=null)
            pageNo = pageDTO.getPageNo();
        else
            pageDTO.setPageNo(0);

        if(pageDTO.getAmount()!=null)
            amount = pageDTO.getAmount();
        else
            pageDTO.setAmount(10);

        Pageable pageable = PageRequest.of(pageNo,amount, Sort.by("id").descending());
        Page<Memo> page =  memoRepository.findAll(pageable);
        PageBlock pageBlock = new PageBlock(pageDTO,page);
        System.out.println("pageBlock " + pageBlock);
        model.addAttribute("page",page);
        model.addAttribute("list",page.getContent());
        model.addAttribute("pageBlock",pageBlock);

//        model.addAttribute("list",memoDAO.selectAll());
//        model.addAttribute("list",memoRepository.findAll());

        //뷰
    }

    @GetMapping("/update")
    public void memo_update(Long id,Model model) throws SQLException {
        log.info("GET /memo/update...." + id);
//        MemoDTO memoDTO = memoDAO.selectOne(id);
        Optional<Memo> memoOp = memoRepository.findById(id);

        if(memoOp.isPresent())
            model.addAttribute("dto",memoOp.get());
        else
            ;
    }
    @PostMapping("/update")
    public String memo_update_post(MemoDTO dto,Model model,RedirectAttributes redirectAttributes) throws SQLException {
        log.info("GET /memo/update...." + dto);
        //1 파라미터
        //2 유효성
        //3 서비스(수정)
//        int result = memoDAO.update(dto);
        Memo memo = Memo.builder()
                .id(dto.getId())
                .text(dto.getText())
                .title(dto.getTitle())
                .writer(dto.getWriter())
                .createAt(LocalDateTime.now())
                .build();
        memoRepository.save(memo);
        redirectAttributes.addFlashAttribute("message",dto.getId() + " 업데이트 성공!");

        //4 뷰로이동(+값 , +메시지)

        return "redirect:/memo/list";
    }

    @GetMapping("/delete")
    public String memo_delete(Long id,RedirectAttributes redirectAttributes) throws SQLException {
        log.info("GET /memo/delete...." + id);

//        int result = memoDAO.delete(id);
        memoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message",id + " 삭제 성공!");

        return "redirect:/memo/list";
    }



}
