package com.example.demo.Controller;


import com.example.demo.Dtos.PersonDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/th")
public class ThymeleafTestController {
    @GetMapping("/index")
    public void index(@RequestParam Boolean isAuth, Model model){
        log.info("GET /th/index..");

        //기본
        model.addAttribute("name","HongGilDong");
        //DTO
        model.addAttribute("dto",new PersonDTO("남길동",55,"대구"));
        //LIST<DTO>
        List<PersonDTO> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(new PersonDTO("TEST"+i,i,""));
        }
        model.addAttribute("list",list);
        //분기처리
        log.info("is auth ? "  + isAuth);
        model.addAttribute("isAuth",isAuth);

        //
    }
//    @GetMapping("/fragments")
//    public void fragment_test(Model model){
//        log.info("GET /th/fragments");
//    }

}
