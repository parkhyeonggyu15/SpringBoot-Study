package com.example.demo.Domain.Common.Dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoDTO {

    private Long id;

    @NotBlank(message = "TITLE 는 필수 항목입니다.")
    private String title;
    @NotBlank(message = "WRITER 는 필수 항목입니다.")
    @Email(message="example@example.com 형식으로 입력하세요")
    private String writer;
    @NotBlank(message = "TEXT 는 필수 항목입니다.")
    private String text;

    private LocalDateTime createAt;


}
