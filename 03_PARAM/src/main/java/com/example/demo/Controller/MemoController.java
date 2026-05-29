package com.example.demo.Controller;

import com.example.demo.Dtos.MemoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/memo")
public class MemoController {

    @GetMapping("/add")
    public void memoAdd() {
        log.info("GET /memo/add...");
    }
    @PostMapping("/add")
    public void memoAddPost(MemoDTO memoDTO) {
        //1. 파라미터 받기
        log.info("POST /memo/add..." + memoDTO);
        //2. 유효성 검증

        //3. 서비스 실행

        //4. 뷰로 이동(+값)

    }

}
