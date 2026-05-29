package com.example.demo.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * ============================================================
 *  [EX] 예외 처리(Exception Handling) 실습 문제
 * ============================================================
 *  - 각 문제의 주석(요청/조건/출력)을 읽고, TODO 부분을 직접 채우세요.
 *  - 정답이 없는 스켈레톤입니다. 빈 부분을 채워 동작시키면 됩니다.
 *  - 연동 화면(JSP)은 이미 작성되어 있습니다. (ex/error.jsp, ex/calc.jsp)
 *  - 서버 포트 : 8090 (application.properties)
 *
 *  [핵심 개념]
 *  - throw : 예외를 강제로 발생시킨다.
 *  - @ExceptionHandler : "이 컨트롤러 안에서" 발생한 특정 예외를 처리한다(지역 처리).
 *  - @ControllerAdvice : "모든 컨트롤러"에서 발생한 예외를 한곳에서 처리한다(전역 처리).
 *  - @ResponseStatus  : 사용자 정의 예외에 HTTP 상태코드를 매핑한다.
 *  - 처리 우선순위 : 컨트롤러 지역 @ExceptionHandler  >  전역 @ControllerAdvice
 *
 *  [테스트 방법]
 *  - 브라우저 주소창에 URL 입력 후, 화면 또는 콘솔(log) 로 결과 확인
 * ============================================================
 */
@Controller
@Slf4j
@RequestMapping("/ex")
public class ExController {

    /*
     * [EX01] 예외를 강제로 발생시키기 (throw)
     * - 요청 : GET /ex/ex01
     * - 조건 : 10 을 0 으로 나눠 ArithmeticException 을 발생시킨다.
     * - 결과 : 처리기가 없으면 전역(GlobalExceptionHandler)로 넘어가 global_error 화면 표시
     *
     *  [HINT] int result = 10 / 0;
     */
    @GetMapping("/ex01")
    public void ex01(){
        log.info("GET /ex/ex01...");
        // TODO: 0 으로 나눠 ArithmeticException 발생시키기
    }


    /*
     * [EX02] 컨트롤러 "지역" 예외 처리 @ExceptionHandler + 화면 출력
     * - 조건 : 이 컨트롤러에서 발생한 ArithmeticException 을 잡아서
     *          예외 메시지를 Model 에 "errorMsg" 로 담고 ex/error.jsp 로 이동한다.
     * - 결과 : EX01 / EX06 에서 발생한 산술예외가 이 메서드에서 처리되어 화면에 메시지 표시
     *
     *  [HINT]
     *   - 메서드 위에 @ExceptionHandler(ArithmeticException.class)
     *   - 파라미터 : (Exception e, Model model)
     *   - model.addAttribute("errorMsg", e.getMessage());
     *   - return "ex/error";
     */
    // TODO: @ExceptionHandler 애너테이션 작성
    public String handleArithmetic(/* TODO: Exception e, Model model */){
        // TODO: 예외 메시지를 model 에 담고 ex/error 반환
        return "ex/error";
    }


    /*
     * [EX03] 한 핸들러로 "여러 예외 타입" 처리하기
     * - 요청 : GET /ex/ex03/abc   (숫자가 아니면 NumberFormatException 발생)
     * - 조건 : 경로변수 num 을 int 로 변환한다(Integer.parseInt).
     *          NumberFormatException, NullPointerException 두 가지를
     *          하나의 @ExceptionHandler 로 처리한다.
     * - 결과 : ex/error.jsp 에 "숫자만 입력 가능합니다" 같은 메시지 표시
     *
     *  [HINT]
     *   - @ExceptionHandler({NumberFormatException.class, NullPointerException.class})
     */
    @GetMapping("/ex03/{num}")
    public void ex03(@PathVariable String num){
        log.info("GET /ex/ex03... num=" + num);
        // TODO: int n = Integer.parseInt(num);  -> 숫자가 아니면 예외 발생
    }

    // TODO: NumberFormatException, NullPointerException 을 함께 처리하는 핸들러 작성
    // public String handleParse(Exception e, Model model){ ... return "ex/error"; }


    /*
     * [EX04] 사용자 정의 예외 + @ResponseStatus
     * - 요청 : GET /ex/ex04?id=0
     * - 조건 : id 가 0 이하이면 MyBizException("잘못된 ID 입니다") 를 throw 한다.
     *          (MyBizException 클래스의 @ResponseStatus 도 직접 작성 → Exception 패키지)
     * - 결과 : 지정한 HTTP 상태코드와 함께 예외 처리
     *
     *  [HINT]
     *   - if(id <= 0) throw new MyBizException("잘못된 ID 입니다");
     */
    @GetMapping("/ex04")
    public void ex04(@RequestParam int id){
        log.info("GET /ex/ex04... id=" + id);
        // TODO: id 가 0 이하이면 MyBizException 발생시키기
    }


    /*
     * [EX05] 전역 예외 처리 @ControllerAdvice 로 넘기기
     * - 요청 : GET /ex/ex05
     * - 조건 : 이 컨트롤러에는 처리기가 "없는" 예외(예: IllegalStateException)를 발생시킨다.
     *          → 지역 핸들러가 없으므로 전역 GlobalExceptionHandler 가 처리하게 된다.
     * - 결과 : global_error 화면 표시
     *
     *  [참고] GlobalException/GlobalExceptionHandler.java 의 @ControllerAdvice 가 받는다.
     */
    @GetMapping("/ex05")
    public void ex05(){
        log.info("GET /ex/ex05...");
        // TODO: throw new IllegalStateException("전역에서 처리될 예외");
    }


    /*
     * ============================================================
     * [EX06] JSP 폼 연동 + 예외 메시지 화면 출력  (★ 화면 연동 문제)
     * ============================================================
     * - 연동 화면 : /WEB-INF/views/ex/calc.jsp  (이미 작성되어 있음)
     *
     * - 요청1 : GET  /ex/calc            -> 나눗셈 입력 폼 표시
     * - 요청2 : POST /ex/calc (num, div) -> num/div 계산, div=0 이면 예외 발생
     *
     * - 동작 조건
     *   1) num, div 파라미터를 받아 나눗셈 결과를 model 에 "result" 로 담아 ex/calc 로 돌아간다.
     *   2) div 가 0 이면 ArithmeticException 이 발생한다.
     *      → EX02 의 @ExceptionHandler(ArithmeticException) 가 잡아서 ex/error.jsp 로 이동,
     *        화면에 "/ by zero" 같은 예외 메시지가 표시된다.
     *
     * - 화면 테스트 : http://localhost:8090/ex/calc 접속 → 10 / 0 입력 후 [계산]
     */
    @GetMapping("/calc")
    public void calc_get(){
        log.info("GET /ex/calc... 입력 폼 표시");
        // return 없음 -> /WEB-INF/views/ex/calc.jsp
    }

    @PostMapping("/calc")
    public String calc_post(@RequestParam int num, @RequestParam int div, Model model){
        log.info("POST /ex/calc... " + num + " / " + div);
        // TODO: 나눗셈 결과를 model 에 "result" 로 담기 (div=0 이면 자동으로 예외 발생)
        //       model.addAttribute("result", num / div);
        return "ex/calc";
    }

}
