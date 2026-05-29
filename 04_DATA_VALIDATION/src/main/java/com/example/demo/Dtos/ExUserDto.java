package com.example.demo.Dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================
 *  [EX] 유효성 검증 실습용 DTO
 * ============================================================
 *  - 아래 각 필드에 알맞은 제약 애너테이션을 직접 작성하세요.
 *  - 메시지(message=...) 도 함께 작성합니다.
 *  - 참고할 애너테이션 : @NotNull, @NotBlank, @Min, @Max, @Email, @Size, @Pattern 등
 *  - 참고 예시는 같은 패키지의 MemoDto 를 보세요.
 * ============================================================
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExUserDto {

    /*
     * [필드1] username
     * - 조건 : 공백 불가(필수). 비어 있으면 "이름은 필수입니다." 메시지
     * - TODO: @NotBlank(message="...") 작성
     */
    private String username;

    /*
     * [필드2] age
     * - 조건 : 최소 10 이상, 최대 120 이하
     *          (각각 "10세 이상만 가입 가능합니다.", "나이가 올바르지 않습니다." 메시지)
     * - TODO: @Min, @Max 작성
     */
    private int age;

    /*
     * [필드3] email
     * - 조건 : 공백 불가 + 이메일 형식
     *          ("이메일은 필수입니다.", "이메일 형식이 올바르지 않습니다.")
     * - TODO: @NotBlank, @Email 작성
     */
    private String email;

    /*
     * [필드4] password
     * - 조건 : 길이 8 ~ 20 자
     *          ("비밀번호는 8~20자여야 합니다.")
     * - TODO: @Size(min=, max=, message="...") 작성
     */
    private String password;
}
