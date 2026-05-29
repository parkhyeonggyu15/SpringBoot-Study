package com.example.demo.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileNotFoundException;

@Controller
@Slf4j
@RequestMapping("/except")
public class ExceptionTestController {

//    @ExceptionHandler(FileNotFoundException.class)
//    public String exceptionHandler_1(Exception e, Model model){
//        log.error("ExceptionTestController ex1 : " + e);
//        model.addAttribute("e",e);
//        return "except/error1";
//    }
//    @ExceptionHandler(ArithmeticException.class)
//    public String exceptionHandler_2(Exception e, Model model){
//        log.error("ExceptionTestController ex2 : " + e);
//        model.addAttribute("e",e);
//        return "except/error2";
//    }

    @GetMapping("/ex1")
    public void ex1() throws FileNotFoundException {
        log.info("GET /except/ex1...");
        throw new FileNotFoundException("파일을 찾을 수 없습니다..");
    }
    @GetMapping("/ex2/{num}/{div}")
    public String ex2(
            @PathVariable int num,
            @PathVariable int div,
            Model model
    ) throws ArithmeticException
    {
        log.info("GET /except/ex2..." );
        model.addAttribute("result",(num/div));
        return "except/ex2";
    }

}
