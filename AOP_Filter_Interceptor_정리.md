# Spring Boot - AOP / Filter / Interceptor

날짜: 2026-06-16

---

## 1. AOP (Aspect-Oriented Programming)

### 개념

비즈니스 로직과 **공통 관심사(로깅, 보안, 트랜잭션 등)를 분리**하는 프로그래밍 패러다임이다.
기존 코드를 수정하지 않고 특정 시점(메서드 실행 전/후 등)에 부가 기능을 **횡단으로 삽입**할 수 있다.

### 핵심 용어

| 용어 | 설명 |
|---|---|
| **Aspect** | 공통 관심사를 모듈화한 클래스 (`@Aspect`) |
| **JoinPoint** | Advice가 적용될 수 있는 지점 (메서드 호출 시점 등) |
| **Pointcut** | 어떤 JoinPoint에 Advice를 적용할지 결정하는 표현식 |
| **Advice** | 실제로 실행되는 공통 로직 (`@Before`, `@After`, `@Around`) |
| **Weaving** | Aspect를 타겟 객체에 적용하는 과정 |

### Advice 종류

| 어노테이션 | 실행 시점 |
|---|---|
| `@Before` | 타겟 메서드 실행 **이전** |
| `@After` | 타겟 메서드 실행 **이후** (예외 여부 관계없이) |
| `@Around` | 타겟 메서드 실행 **전/후 모두** 제어 가능 |
| `@AfterReturning` | 정상 반환 이후 |
| `@AfterThrowing` | 예외 발생 이후 |

---

### 주요 코드

**LoggingAspect.java**

```java
@Component
@Slf4j
@Aspect
public class LoggingAspect {

    @After("execution(* com.example.demo.Domain.Common.Service.*.*(..))")
    public void logginAfter(JoinPoint joinPoint){
        log.info("[AOP] AFTER..." + joinPoint);
    }

    @Around("execution(* com.example.demo.Domain.Common.Service.*.*(..))")
    public Object logginAround(ProceedingJoinPoint pjp) throws Throwable {
        long start_time = System.currentTimeMillis();
        log.info("[AOP] AROUND BEFORE");

        Object returnValue = pjp.proceed();
        log.info("타겟 함수 리턴값 : " + returnValue);

        log.info("[AOP] AROUND AFTER");
        long end_time = System.currentTimeMillis();
        log.info("[AOP] 소요시간 : " + (end_time-start_time)+" ms");
        return returnValue;
    }
}
```

> **`@Aspect`** — 이 클래스가 AOP의 Aspect임을 선언. `@Component`와 함께 써야 스프링 빈으로 등록된다.
>
> **Pointcut 표현식 `execution(* com.example.demo.Domain.Common.Service.*.*(..))`** — `Service` 패키지 안의 모든 클래스(`*`)의 모든 메서드(`*`)에 적용. 파라미터는 `(..)` = 어떤 파라미터든 허용.
>
> **`@After` vs `@Around`** — `@After`는 타겟 메서드가 끝난 뒤 단순히 끼어들기만 한다. `@Around`는 `ProceedingJoinPoint`를 통해 타겟 메서드를 직접 실행(`pjp.proceed()`)하고 리턴값까지 가로챌 수 있어서 가장 강력하다.
>
> **실행 순서** — `@Around`의 BEFORE → 타겟 메서드 → `@Around`의 AFTER → `@After` 순으로 동작한다.

---

**AopTestService.java**

```java
@Service
@Slf4j
public class AopTestService {

    public String run1(String param){
        log.info("[AopTestService] run1 invoke...!" );
        return "param : " +param;
    }

    public String run2(String param){
        log.info("[AopTestService] run2 invoke...!");
        return "param : "+ param;
    }
}
```

> AOP가 적용될 **타겟 클래스**. 코드 자체에는 AOP 관련 코드가 전혀 없다. 이것이 AOP의 핵심 — 비즈니스 로직과 공통 관심사(로깅)가 완전히 분리된다.

---

**AopTestServiceTest.java**

```java
@SpringBootTest
class AopTestServiceTest {

    @Autowired
    private AopTestService aopTestService;

    @Autowired
    private MemoService memoService;

    @Test
    @Transactional
    public void t1() throws Exception {
        aopTestService.run1("HELLO1");
        aopTestService.run2("HELLO2");
        memoService.memoRegistration(MemoDTO.builder()
            .title("t1").text("text1").writer("a@a.com").build());
    }
}
```

> `@SpringBootTest`로 실제 스프링 컨텍스트를 띄워서 AOP가 실제로 작동하는지 확인한다. `memoService`도 같이 호출해 Pointcut이 `Service` 패키지 전체에 걸리는지 검증한다.

---

## 2. Filter & Interceptor

### 개념 비교

요청이 들어올 때의 처리 흐름:

```
Client → [Filter] → DispatcherServlet → [Interceptor] → Controller → [Interceptor] → [Filter] → Client
```

| 구분 | **Filter** | **Interceptor** |
|---|---|---|
| 소속 | 서블릿 컨테이너 (Jakarta EE 표준) | 스프링 MVC |
| 적용 범위 | DispatcherServlet **이전** | DispatcherServlet **이후** |
| 스프링 빈 접근 | 불가 (기본) | 가능 |
| 주 사용 용도 | 인코딩, 보안(CORS, XSS), 로깅 | 인증/인가, 로깅, 공통 컨트롤러 처리 |
| 등록 방법 | `@WebFilter` 또는 `FilterRegistrationBean` | `WebMvcConfigurer.addInterceptors()` |

---

### 주요 코드 — Filter

**MemoFilter.java**

```java
@WebFilter("/memo/*")
@Slf4j
public class MemoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("[FILTER] MemoFilter START");
        chain.doFilter(request, response);
        log.info("[FILTER] MemoFilter END");
    }
}
```

> **`chain.doFilter(request, response)`** — 이 줄이 다음 필터 또는 서블릿으로 요청을 넘기는 핵심 코드. 이 줄 **위**가 요청 전처리, **아래**가 응답 후처리.
>
> **`@WebFilter` 사용 시 주의** — `@WebFilter` 단독으로는 스프링이 스캔하지 못한다. 반드시 메인 클래스에 `@ServletComponentScan`을 붙이거나, 아래의 `FilterConfig` 방식으로 등록해야 한다.

---

**FilterConfig.java**

```java
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MemoFilter> memoFilter(){
        FilterRegistrationBean<MemoFilter> registrationBean =
            new FilterRegistrationBean<MemoFilter>();
        registrationBean.setFilter(new MemoFilter());
        registrationBean.addUrlPatterns("/memo/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
```

> **`FilterRegistrationBean`** — 스프링 부트에서 필터를 코드로 등록하는 방법. `@WebFilter`와 달리 `@ServletComponentScan` 없이도 작동하며, `.setOrder()`로 여러 필터의 **실행 순서를 제어**할 수 있어 더 권장되는 방식.

---

### 주요 코드 — Interceptor

**MemoInterceptor.java**

```java
@Component
@Slf4j
public class MemoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("[INTERCEPTOR] Memo컨트롤러 실행 전 : " + request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception {
        log.info("[INTERCEPTOR] Memo컨트롤러 실행 후 : " + request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        log.info("[INTERCEPTOR] Memo컨트롤러 요청-응답 완료 후 : " + request.getRequestURI());
    }
}
```

> **`preHandle`** — 컨트롤러 실행 **전**에 호출. 반환값이 `true`면 다음 단계로 진행, `false`면 요청을 여기서 차단한다. 인증/인가 체크에 주로 사용.
>
> **`postHandle`** — 컨트롤러가 실행된 **후, 뷰 렌더링 전**에 호출. `ModelAndView`에 접근 가능해 뷰에 데이터를 추가로 넣을 수 있다.
>
> **`afterCompletion`** — 뷰 렌더링까지 **모두 완료된 후** 호출. 예외가 발생해도 항상 실행되므로 리소스 정리에 적합하다.

---

**WebMvcConfig.java**

```java
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    MemoInterceptor memoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memoInterceptor)
                .addPathPatterns("/memo/**")
                .excludePathPatterns("", "");
    }
}
```

> **`WebMvcConfigurer`** — 스프링 MVC 설정을 커스터마이징하는 인터페이스. `addInterceptors()`를 오버라이드해 인터셉터를 등록한다.
>
> **`.addPathPatterns("/memo/**")`** — 인터셉터가 적용될 URL 패턴. `**`는 하위 경로 전체를 의미한다.
>
> **`.excludePathPatterns()`** — 정적 리소스 경로(`/css/**`, `/js/**` 등)는 여기서 제외해야 인터셉터가 불필요하게 실행되지 않는다.

---

## 3. 전체 실행 흐름 요약

```
HTTP 요청
  ↓
[MemoFilter] doFilter 전처리 (START 로그)
  ↓
DispatcherServlet
  ↓
[MemoInterceptor] preHandle → 컨트롤러 진입 여부 결정
  ↓
MemoController 실행
  ↓
[AOP - @Around/@After] 서비스 메서드 전후 처리
  ↓
MemoService 실행
  ↓
[MemoInterceptor] postHandle → 뷰 렌더링 전
  ↓
뷰 렌더링
  ↓
[MemoInterceptor] afterCompletion → 렌더링 완료 후
  ↓
[MemoFilter] doFilter 후처리 (END 로그)
  ↓
HTTP 응답
```
