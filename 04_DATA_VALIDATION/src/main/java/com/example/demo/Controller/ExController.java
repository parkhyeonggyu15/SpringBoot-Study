package com.example.demo.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * ============================================================
 *  [EX] 유효성 검증(Validation) 실습 문제
 * ============================================================
 *  - 각 문제의 주석(요청/조건/출력)을 읽고, TODO 부분을 직접 채우세요.
 *  - 정답이 없는 스켈레톤입니다. 빈 부분을 채워 동작시키면 됩니다.
 *  - 검증 대상 DTO 는 ExUserDto 입니다. (Domain.Common.Dto.ExUserDto)
 *  - 서버 포트 : 8090 (application.properties)
 *
 *  [참고]
 *  - 컨트롤러에서 @Valid + BindingResult 로 검증 결과를 받는다.
 *  - BindingResult 는 반드시 검증 대상(@Valid 붙은 객체) "바로 뒤" 에 와야 한다.
 *  - 제약 애너테이션은 DTO(ExUserDto) 에 작성한다.
 *
 *  [테스트 방법]
 *  - GET  : 브라우저 주소창에 URL 입력
 *  - POST : PowerShell 에서 curl 사용 (각 문제에 예시 작성)
 *  - 결과는 콘솔(log) 출력으로 확인
 * ============================================================
 */
@Controller
@Slf4j
@RequestMapping("/ex")
public class ExController {

    /*
     * [EX01] @Valid + BindingResult 기본 형태
     * - 요청 : GET /ex/ex01?username=hong&age=20&email=hong@test.com&password=abcd1234
     * - 조건 : ExUserDto 를 검증 대상으로 받고, 검증 결과를 BindingResult 로 받는다.
     *          오류가 있으면 hasErrors() 가 true 가 되어야 한다.
     * - 출력 : EX01 오류 발생여부 : false   (값이 올바를 때)
     *
     *  [HINT]
     *   - 파라미터에 @Valid ExUserDto dto, BindingResult bindingResult 를 선언
     */
    @GetMapping("/ex01")
    public void ex01(/* TODO: @Valid 검증 대상 + BindingResult 받기 */){
        // TODO: log.info("EX01 오류 발생여부 : " + bindingResult.hasErrors());
    }


    /*
     * [EX02] 검증 오류 메시지를 모두 콘솔에 출력하기
     * - 요청 : GET /ex/ex02   (일부러 값을 비워서 오류 유발)
     * - 조건 : 검증 오류가 있을 때, FieldError 를 순회하며
     *          "필드명 / 메시지" 를 한 줄씩 log 로 출력한다.
     * - 출력 : EX02 field : username, message : 이름은 필수입니다.   (예시)
     *
     *  [HINT]
     *   - if(bindingResult.hasErrors()){ for(FieldError e : bindingResult.getFieldErrors()){ ... } }
     */
    @GetMapping("/ex02")
    public void ex02(/* TODO: @Valid 대상 + BindingResult 받기 */){
        // TODO: 오류가 있으면 FieldError 를 순회하며
        //       log.info("EX02 field : " + e.getField() + ", message : " + e.getDefaultMessage());
    }


    /*
     * [EX03] 오류 메시지를 Model 에 담아 뷰로 전달하기
     * - 요청 : GET /ex/ex03   (잘못된 값으로 호출)
     * - 조건 : 각 FieldError 의 메시지를 Model 에 (필드명 -> 메시지) 형태로 담는다.
     *          그래야 JSP 에서 ${username} 처럼 오류 메시지를 출력할 수 있다.
     * - 출력 : 콘솔 + Model 속성 추가
     *
     *  [HINT]
     *   - model.addAttribute(e.getField(), e.getDefaultMessage());
     */
    @GetMapping("/ex03")
    public void ex03(/* TODO: @Valid 대상 + BindingResult + Model 받기 */){
        // TODO: 오류 순회하며 model 에 (필드명, 메시지) 담기
    }


    /*
     * [EX04] POST 요청 + form 데이터 검증
     * - 요청 : POST /ex/ex04  (body: username=&age=5&email=bad&password=1)
     * - 조건 : POST 로 전달된 form 데이터를 ExUserDto 로 받아 검증한다.
     * - 출력 : EX04 오류 개수 : 4   (예시 - 제약조건에 따라 달라짐)
     *
     *  [PowerShell 테스트]
     *    curl -Method POST "http://localhost:8090/ex/ex04" -Body "username=&age=5&email=bad&password=1"
     *
     *  [HINT]
     *   - 매핑 애너테이션을 @PostMapping("/ex04") 로 작성
     *   - bindingResult.getErrorCount() 로 오류 개수 확인
     */
    // TODO: POST 매핑 애너테이션 작성
    public void ex04(/* TODO: @Valid 대상 + BindingResult 받기 */){
        // TODO: log.info("EX04 오류 개수 : " + bindingResult.getErrorCount());
    }


    /*
     * [EX05] 특정 필드의 오류만 확인하기
     * - 요청 : GET /ex/ex05?username=hong&age=20&email=bad-email&password=abcd1234
     * - 조건 : email 형식이 틀렸을 때, "email" 필드에 오류가 있는지 검사한다.
     * - 출력 : EX05 email 오류 여부 : true
     *
     *  [HINT]
     *   - bindingResult.hasFieldErrors("email")
     *   - bindingResult.getFieldError("email").getDefaultMessage()
     */
    @GetMapping("/ex05")
    public void ex05(/* TODO: @Valid 대상 + BindingResult 받기 */){
        // TODO: log.info("EX05 email 오류 여부 : " + bindingResult.hasFieldErrors("email"));
    }


    /*
     * ============================================================
     * [EX06] JSP 폼 연동 + 검증 메시지 화면 출력  (★ 화면 연동 문제)
     * ============================================================
     * - 연동 화면 : /WEB-INF/views/ex/form.jsp  (이미 작성되어 있음)
     *
     * - 요청1 : GET  /ex/form   -> 입력 폼 화면을 보여준다.
     * - 요청2 : POST /ex/form   -> 입력값을 검증하고, 오류 메시지를 화면에 출력한다.
     *
     * - 동작 조건
     *   1) 검증 대상(ExUserDto)을 @Valid 로 받고, 결과를 BindingResult 로 받는다.
     *   2) 입력값이 화면에 다시 채워지도록 dto 를 Model 에 담는다.  (model.addAttribute("dto", dto))
     *   3) 오류가 있으면 FieldError 를 순회하며
     *      Model 에 (필드명 -> 메시지) 로 담는다.  -> JSP의 ${username},${age},${email},${password} 위치에 출력됨
     *   4) 오류가 있으면 다시 폼 화면("ex/form")으로, 없으면 완료 페이지("ex/form_ok")로 이동한다.
     *
     * - 화면 테스트 : 브라우저에서 http://localhost:8090/ex/form 접속 후
     *                값을 비우거나 잘못 입력하고 [가입] 버튼 클릭 -> 각 항목 옆에 메시지 표시 확인
     */
    @GetMapping("/form")
    public void form_get(){
        log.info("GET /ex/form... 입력 폼 표시");
        // return 없음 -> /WEB-INF/views/ex/form.jsp 로 이동(자동)
    }

    @PostMapping("/form")
    public String form_post(/* TODO 1: @Valid 검증 대상 + BindingResult + Model 받기 */){
        // log.info("POST /ex/form..." + dto);

        // TODO 2: 입력값 유지를 위해 dto 를 model 에 담기
        //         model.addAttribute("dto", dto);

        // TODO 3: 오류가 있으면 FieldError 순회하며 (필드명 -> 메시지) 를 model 에 담기
        //         if(bindingResult.hasErrors()){
        //             for(FieldError e : bindingResult.getFieldErrors()){
        //                 model.addAttribute(e.getField(), e.getDefaultMessage());
        //             }
        //             return "ex/form";   // 오류 시 폼으로 복귀
        //         }

        // TODO 4: 검증 통과 시 완료 화면으로 이동
        return "ex/form_ok";
    }

}
