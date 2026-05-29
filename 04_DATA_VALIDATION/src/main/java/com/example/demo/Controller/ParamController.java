package com.example.demo.Controller;

import com.example.demo.Dtos.PersonDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@Slf4j
@RequestMapping(value = "/param")
public class ParamController {

    //-----------------------------
    // 사용자 - 파라미터 -> 서버 (잘받는지 확인)
    //-----------------------------
//    @RequestMapping(value="/p01",method = RequestMethod.GET)
    @GetMapping("/p01")
    public String paramHandler_1(@RequestParam(required = false) String name){
        log.info("GET /param/p01.."+name);
        return "param/page01";
        //WEB-INF/views/param/page01.jsp
    }
//    @RequestMapping(value="/p02",method = RequestMethod.POST)
    @PostMapping("/p02")
    public String paramHandler_2(@RequestParam(name="username") String name){
        log.info("GET /param/p02.."+name);
        return "param/page02";
        //WEB-INF/views/param/page02.jsp
    }
    @RequestMapping(value="/p03",method = RequestMethod.GET)
    public String paramHandler_3(String name){
        log.info("GET /param/p03.."+name);
        return "param/page03";
        //WEB-INF/views/param/page02.jsp
    }
    @RequestMapping(value="/p04",method = RequestMethod.GET)
    public String paramHandler_4(
            @RequestParam String name,
            @RequestParam int age,
            @RequestParam String addr
    ){
        log.info("GET /param/p04.."+name+" " + age + " " + addr);
        return "param/page04";
    }

    @RequestMapping(value="/p05",method = RequestMethod.GET)
    public String paramHandler_5(@ModelAttribute PersonDTO dto){
        log.info("GET /param/p05.."+dto);
        return "param/page05";
    }
    @RequestMapping(value="/p06",method = RequestMethod.GET)
    public String paramHandler_6(PersonDTO dto){
        log.info("GET /param/p06.."+dto);
        return "param/page06";
    }
    @RequestMapping(value="/p07/{name}/{age}/{addr}",method = RequestMethod.GET)
    public String paramHandler_7(
            @PathVariable String name,
            @PathVariable int age,
            @PathVariable String addr
    ){
        log.info("GET /param/p07.."+name+" " + age + " " + addr);
        return "param/page07";
    }
    @RequestMapping(value="/p08/{name}/{age}/{addr}",method = RequestMethod.GET)
    public String paramHandler_8(PersonDTO dto){
        log.info("GET /param/p08.."+dto);
        return "param/page08";
    }
    //@RequestParam : 동기요청 파라미터 처리 / html form 기반 전달되는 파라미터 처리(JS의 form-data도 받기가능,JSON Type 받기불가)
    //@RequestBody  : 비동기요청 파라미터 처리 / json,filedata등 전달되는 파라미터 처리(html form 처리가능)
    @RequestMapping(value = "/p09", method = RequestMethod.POST)
    public String paramHandler_rp(@ModelAttribute PersonDTO dto) {
        log.info("GET /param/p09.." + dto);
        return "param/page09";
    }
    //POSTMAN 설정
    //Content-Type : application/json
    //Body : row 선택 ->  JSON TYPE FORMATTING
    @RequestMapping(value = "/p10", method = RequestMethod.POST)
    public String paramHandler_rb(@RequestBody PersonDTO dto){
        log.info("GET /param/p10.."+dto);
        return "param/page10";
    }

    //-----------------------------
    // 사용자(잘받는지 확인) <- 전달하는 값 -  서버
    //-----------------------------
    @RequestMapping(value = "/p11", method = RequestMethod.GET)
    public String paramHandler_11(PersonDTO dto, Model model){
        log.info("GET /param/p11.."+dto);
        // 1 파라미터 받기(o)

        // 2 유효성 검증(x)

        // 3 서비스(x)

        // 4 뷰로이동(+값 전달)
        model.addAttribute("name",dto.getName());
        model.addAttribute("age",dto.getAge());
        model.addAttribute("addr",dto.getAddr());
        model.addAttribute("now", LocalDateTime.now());
        return "param/page11";
    }
    @RequestMapping(value = "/p12", method = RequestMethod.GET)
    public String paramHandler_12(PersonDTO dto, Model model){
        log.info("GET /param/p12.."+dto);
        // 1 파라미터 받기(o)

        // 2 유효성 검증(x)

        // 3 서비스(x)

        // 4 뷰로이동(+값 전달)
        model.addAttribute("dto",dto);;
        model.addAttribute("now", LocalDateTime.now());
        return "param/page12";
    }
    @RequestMapping(value = "/p13", method = RequestMethod.GET)
    public ModelAndView paramHandler_13(PersonDTO dto){
        log.info("GET /param/p13.."+dto);
        // 1 파라미터 받기(o)

        // 2 유효성 검증(x)

        // 3 서비스(x)

        // 4 뷰로이동(+값 전달)
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("dto",dto);;
        modelAndView.addObject("now", LocalDateTime.now());
        modelAndView.setViewName("param/page13");
        return modelAndView;
    }
    //-----------------------------
    // Servlet Class 사용해보기
    // HttpServletRequest request
    // HttpServletResponse response
    //-----------------------------
    @RequestMapping(value = "/p14", method = RequestMethod.GET)
    public void paramHandler_14(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {

        log.info("GET /param/p14..");
        // 1 파라미터 받기(o)
        String name = request.getParameter("name");
        int age = -1;
        if(request.getParameter("age")!= null)
            age = Integer.parseInt(request.getParameter("age"));
        String addr = request.getParameter("addr");

        // 2 유효성 검증(x)

        // 3 서비스(x)

        // 4 뷰로이동(+값 전달)
        PersonDTO dto = new PersonDTO(name,age,addr);
        request.setAttribute("dto",dto);
        request.setAttribute("now", LocalDateTime.now());
        request.getRequestDispatcher("/WEB-INF/views/param/page14.jsp").forward(request,response);
    }

    //-----------------------------
    // Forward / Redirect
    //-----------------------------
    @GetMapping("/forward/init")
    public String forward_init_handler(Model model){
        log.info("GET /param/forward/init...");
        model.addAttribute("init","init_value");
        return "forward:/param/forward/step1";
    }
    @GetMapping("/forward/step1")
    public String forward_step1_handler(Model model){
        log.info("GET /param/forward/step1...");
        model.addAttribute("step1","step1_value");
        return "forward:/param/forward/step2";
    }
    @GetMapping("/forward/step2")
    public String forward_step2_handler(Model model){
        log.info("GET /param/forward/step2...");
        model.addAttribute("step2","step2_value");
        return "param/forward/step2";
    }
    //-----------------------------------------
    @GetMapping("/redirect/init")
    public String redirect_init_handler(Model model, RedirectAttributes redirectAttributes){
        log.info("GET /param/redirect/init...");

//      model.addAttribute("init","init_value");

//      redirectAttributes.addAttribute("init","init_value"); //QueryString
        redirectAttributes.addFlashAttribute("init","init_value"); //session기반
        return "redirect:/param/redirect/step1";
    }
    @GetMapping("/redirect/step1")
    public String redirect_step1_handler(Model model,RedirectAttributes redirectAttributes){
        log.info("GET /param/redirect/step1...");
//        model.addAttribute("step1","step1_value");
        redirectAttributes.addFlashAttribute("step1","step1_value"); //session기반
        return "redirect:/param/redirect/step2";
    }
    @GetMapping("/redirect/step2")
    public String redirect_step2_handler(Model model){
        log.info("GET /param/redirect/step2...");
        model.addAttribute("step2","step2_value");
        return "param/redirect/step2";
    }

}
