package com.example.demo.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ============================================================
 *  [EX] 파라미터 받기 실습 문제 (기본기)
 * ============================================================
 *  - 각 문제의 주석(요청/조건/출력)을 읽고, 메서드 파라미터와 본문의 TODO를 채우세요.
 *  - 정답이 없는 스켈레톤입니다. 빈 부분을 직접 작성하면 됩니다.
 *  - 서버 포트: 8090 (application.properties)
 *
 *  [테스트 방법]
 *  - GET 요청: 브라우저 주소창에 URL 입력
 *  - POST 요청: 콘솔에서 curl 사용 (Windows PowerShell 예시는 각 문제에 적어둠)
 *  - 결과는 콘솔(log) 출력으로 확인
 * ============================================================
 */
@Controller
@Slf4j
@RequestMapping("/ex")
public class ExController {

    /*
     * [EX01] @RequestParam - 단일 파라미터
     * - 요청 : GET /ex/ex01?keyword=spring
     * - 조건 : "keyword" 쿼리 파라미터를 String 으로 받는다.
     * - 출력 : EX01 keyword : spring
     */
    @GetMapping("/ex01")
    public void ex01(/* TODO: keyword 파라미터 받기 */){
        // TODO: log.info("EX01 keyword : " + keyword);
    }


    /*
     * [EX02] @RequestParam - required=false + defaultValue
     * - 요청 : GET /ex/ex02            -> page 없음
     *          GET /ex/ex02?page=3     -> page=3
     * - 조건 : "page" 파라미터를 받되, 없으면 기본값 "1" 이 들어가게 한다.
     * - 출력 : EX02 page : 1   /   EX02 page : 3
     */
    @GetMapping("/ex02")
    public void ex02(/* TODO: page 파라미터 받기 (없으면 기본값 1) */){
        // TODO: log.info("EX02 page : " + page);
    }


    /*
     * [EX03] @RequestParam - 파라미터 이름과 변수 이름이 다를 때
     * - 요청 : GET /ex/ex03?user_name=hong
     * - 조건 : 전달되는 파라미터명은 "user_name" 이지만,
     *          메서드 변수명은 name 으로 받는다.
     * - 출력 : EX03 name : hong
     */
    @GetMapping("/ex03")
    public void ex03(/* TODO: user_name -> name 으로 매핑해서 받기 */){
        // TODO: log.info("EX03 name : " + name);
    }


    /*
     * [EX04] @RequestParam - 여러 개 + 자동 형변환
     * - 요청 : GET /ex/ex04?name=hong&age=20
     * - 조건 : name 은 String, age 는 int 로 받는다. (문자열 -> int 자동변환 확인)
     * - 출력 : EX04 name : hong, age : 20
     */
    @GetMapping("/ex04")
    public void ex04(/* TODO: name(String), age(int) 받기 */){
        // TODO: log.info("EX04 name : " + name + ", age : " + age);
    }


    /*
     * [EX05] @RequestParam Map - 모든 파라미터를 한 번에
     * - 요청 : GET /ex/ex05?a=1&b=2&c=3
     * - 조건 : 어떤 파라미터가 올지 모를 때, 전부 Map<String,Object> 로 받는다.
     * - 출력 : EX05 params : {a=1, b=2, c=3}
     */
    @GetMapping("/ex05")
    public void ex05(/* TODO: 모든 파라미터를 Map 으로 받기 */){
        // TODO: log.info("EX05 params : " + params);
    }


    /*
     * [EX06] @PathVariable - 경로 변수 1개
     * - 요청 : GET /ex/ex06/100
     * - 조건 : URL 경로의 {id} 부분을 int 로 받는다.
     * - 출력 : EX06 id : 100
     */
    @GetMapping("/ex06/{id}")
    public void ex06(/* TODO: 경로변수 id 받기 */){
        // TODO: log.info("EX06 id : " + id);
    }


    /*
     * [EX07] @PathVariable - 경로 변수 여러 개
     * - 요청 : GET /ex/ex07/book/100
     * - 조건 : {category}, {id} 두 경로변수를 받는다.
     * - 출력 : EX07 category : book, id : 100
     */
    @GetMapping("/ex07/{category}/{id}")
    public void ex07(/* TODO: category, id 경로변수 받기 */){
        // TODO: log.info("EX07 category : " + category + ", id : " + id);
    }


    /*
     * [EX08] DTO(커맨드 객체) 바인딩
     * - 요청 : GET /ex/ex08?name=hong&age=20&addr=seoul
     * - 조건 : PersonDto(name, age, addr) 로 한 번에 받는다.
     *          (필드명과 파라미터명이 같으면 자동 바인딩됨)
     * - 출력 : EX08 dto : PersonDto(name=hong, age=20, addr=seoul)
     */
    @GetMapping("/ex08")
    public void ex08(/* TODO: PersonDto 로 받기 */){
        // TODO: log.info("EX08 dto : " + dto);
    }


    /*
     * [EX09] POST + @RequestParam (form 데이터)
     * - 요청 : POST /ex/ex09  (body: username=hong)
     * - 조건 : POST 로 전달된 form 파라미터 "username" 을 받는다.
     * - 출력 : EX09 username : hong
     *
     * [PowerShell 테스트]
     *   curl -Method POST "http://localhost:8090/ex/ex09" -Body "username=hong"
     */
    // TODO: POST 요청을 처리하도록 매핑 애너테이션을 작성하세요. (예: @PostMapping("/ex09"))
    public void ex09(/* TODO: username 파라미터 받기 */){
        // TODO: log.info("EX09 username : " + username);
    }

}
