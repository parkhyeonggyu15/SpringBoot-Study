package com.example.demo.Domain.Common.Service;

import com.example.demo.Domain.Common.Dtos.*;
import com.example.demo.Domain.Common.Entity.Book;
import com.example.demo.Domain.Common.Entity.Memo;
import com.example.demo.Domain.Common.Repository.Ex01_BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * [Ex01] 도서 REST 실습 - BookServiceImpl  (학생 작성용 / 문제)
 *  ※ implements 구문은 Ex01_BookService 의 TODO-5 를 먼저 완성한 뒤 붙이세요.
 */
@Service
public class Ex01_BookServiceImpl implements Ex01_BookService/* implements Ex01_BookService */ {

    // TODO-6: Ex01_BookRepository 를 @Autowired 로 주입하세요.
    @Autowired
    private Ex01_BookRepository bookRepository;

    // TODO-7: 등록 — bookRepository.save(dto.toEntity()) 후 true 반환 (@Transactional 적용)
    @Override
    @Transactional(rollbackFor = SQLException.class,transactionManager = "jpaTransactionManager")
    public boolean bookRegistration(Ex01_BookDTO dto) throws Exception {
        bookRepository.save(dto.toEntity());
        return true;
    }

    // TODO-8: 목록 — PageDTO 의 pageNo/amount 기본값 처리(null 이면 0, 10),
    //         PageRequest.of(pageNo, amount, Sort.by("bookCode").descending()) 로 findAll 호출,
    //         반환 Map 에 "page", "pageBlock"(new PageBlock<>(pageDTO, page)), "list"(page.getContent()) 담기
    @Override
    public Map<String, Object> getBookList(PageDTO pageDTO) throws Exception {

        Map<String,Object> returnValue = new HashMap<>();

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

        Pageable pageable = PageRequest.of(pageNo,amount, Sort.by("bookCode").descending());
        Page<Book> page =  bookRepository.findAll(pageable);
        PageBlock pageBlock = new PageBlock(pageDTO,page);
        List<Book> list = page.getContent();

        returnValue.put("page",page);
        returnValue.put("pageBlock",pageBlock);
        returnValue.put("list",list);

        return returnValue;
    }

    @Override
    public boolean removeBook(Long bookCode) throws Exception {
        return false;
    }
    // TODO-9: 단건조회 getBook — findById(bookCode) 존재 시 Ex01_BookDTO.from(...) 아니면 null
    @Override
    public Ex01_BookDTO getBook(Long bookCode) throws Exception {
        Optional<Book> bookOptional = bookRepository.findById(bookCode);
        if(bookOptional.isPresent()){
            return Ex01_BookDTO.from(bookOptional.get());
        }
        return null;
    }
    // TODO-10: 수정 updateBook(save 후 dto.getBookCode()>0) / 삭제 removeBook(deleteById 후 true)
    @Override
    public boolean updateBook(Ex01_BookDTO dto) throws Exception {
        bookRepository.save(dto.toEntity());
        return dto.getBookCode()>0;
    }
}
