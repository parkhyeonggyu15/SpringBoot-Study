package com.example.demo.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping(value = "/param")
public class ParamController {


    @RequestMapping(value = "/p01",method = RequestMethod.GET)
    public String paramHandler_1(@RequestParam(required = false) String name){
        log.info("GET /param/p01.."+name);
        return "param/page01";
        ///WEB-INF/views/param/page01.jsp
    }

    @RequestMapping(value = "/p02",method = RequestMethod.POST)
    public String paramHandler_2(@RequestParam(name = "username") String name){
        log.info("GET /param/p02.."+name);
        return "param/page02";
        ///WEB-INF/views/param/page02.jsp
    }

    @RequestMapping(value = "/p03",method = RequestMethod.GET)
    public String paramHandler_3(String name){
        log.info("GET /param/p03.."+name);
        return "param/page03";
        ///WEB-INF/views/param/page03.jsp
    }
}
