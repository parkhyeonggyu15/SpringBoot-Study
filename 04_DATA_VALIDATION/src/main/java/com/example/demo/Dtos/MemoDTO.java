package com.example.demo.Dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoDTO {
    @Min(value = 10 , message="ID는 10이상의 값부터 시작합니다")
    @Max(value = 65535 , message="ID는 최대 숫자는 65535입니다")
    @NotNull(message = "ID는 필수 항목입니다.")
    private Long id;
    @NotBlank(message = "TITLE는 필수 항목입니다.")
    private String title;
    @Email(message = "example@example.com 형식으로 입력하세요")
    @NotBlank(message = "WRITER는 필수 항목입니다.")
    private String writer;
    @NotBlank(message = "TEXT는 필수 항목입니다.")
    private String text;
    @NotNull(message = "CREATE_AT는 필수 항목입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Future(message = "오늘날짜기준 이후날짜를 입력하세요")
    private LocalDateTime createAt;

}
