package com.example.demo.Domain.Common.Dtos;

import com.example.demo.Domain.Common.Entity.Memo;
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

    public Memo toEntity(){
        return Memo.builder()
                .title(this.title)
                .id(this.id)
                .writer(this.writer)
                .text(this.text)
                .createAt(this.createAt)
                .build();
    }
    public static MemoDTO from(Memo memo){
        return MemoDTO.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .writer(memo.getWriter())
                .text(memo.getText())
                .createAt(memo.getCreateAt())
                .build();
    }

}
