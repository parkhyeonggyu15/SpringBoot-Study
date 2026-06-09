package com.example.demo.Domain.Common.Service;

import com.example.demo.Domain.Common.Dtos.Ex01_BookDTO;
import com.example.demo.Domain.Common.Dtos.PageBlock;
import com.example.demo.Domain.Common.Dtos.PageDTO;
import com.example.demo.Domain.Common.Entity.Book;
import com.example.demo.Domain.Common.Repository.Ex01_BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * [Ex01] 도서 REST 실습 - BookServiceImpl  (학생 작성용 / 문제)
 *  ※ implements 구문은 Ex01_BookService 의 TODO-5 를 먼저 완성한 뒤 붙이세요.
 */
@Service
public class Ex01_BookServiceImpl /* implements Ex01_BookService */ {

    // TODO-6: Ex01_BookRepository 를 @Autowired 로 주입하세요.

    // TODO-7: 등록 — bookRepository.save(dto.toEntity()) 후 true 반환 (@Transactional 적용)

    // TODO-8: 목록 — PageDTO 의 pageNo/amount 기본값 처리(null 이면 0, 10),
    //         PageRequest.of(pageNo, amount, Sort.by("bookCode").descending()) 로 findAll 호출,
    //         반환 Map 에 "page", "pageBlock"(new PageBlock<>(pageDTO, page)), "list"(page.getContent()) 담기

    // TODO-9: 단건조회 getBook — findById(bookCode) 존재 시 Ex01_BookDTO.from(...) 아니면 null

    // TODO-10: 수정 updateBook(save 후 dto.getBookCode()>0) / 삭제 removeBook(deleteById 후 true)

}
