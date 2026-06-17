package com.example.demo.Handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class CustomHandler implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getRequestURI();

        System.out.println("[HANDLER] CustomHandler's handleRequest invoke..");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("memo/add");
        return modelAndView;
    }
}
