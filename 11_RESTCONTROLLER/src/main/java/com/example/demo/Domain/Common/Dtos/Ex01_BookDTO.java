package com.example.demo.Domain.Common.Dtos;

import com.example.demo.Domain.Common.Entity.Book;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [Ex01] 도서 REST 실습 - BookDTO  (학생 작성용 / 문제)
 *  기존 Book 엔티티(bookCode, bookName, publisher, isbn)를 그대로 사용한다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ex01_BookDTO {

    // bookCode 는 수동 PK(자동증가 아님)이므로 등록 시 직접 입력받는다.
    @NotNull(message = "BOOKCODE 는 필수 항목입니다.")
    private Long bookCode;

    // TODO-1: bookName / publisher / isbn 에 @NotBlank 검증 어노테이션을 추가하세요.
    @NotBlank
    private String bookName;
    @NotBlank
    private String publisher;
    @NotBlank
    private String isbn;

    // TODO-2: toEntity() 작성 — this 필드들을 Book.builder()...build() 로 변환해 반환
    public Book toEntity() {
        return  Book.builder()
                .bookCode(this.bookCode)
                .bookName(this.bookName)
                .publisher(this.publisher)
                .isbn(this.isbn)
                .build();// 구현하세요
    }

    // TODO-3: from(Book book) 작성 — Book → Ex01_BookDTO.builder()...build() 로 변환해 반환
    public static Ex01_BookDTO from(Book book) {

        return Ex01_BookDTO.builder()
                .bookCode(book.getBookCode())
                .bookName(book.getBookName())
                .publisher(book.getPublisher())
        .build(); // 구현하세요
    }
}
