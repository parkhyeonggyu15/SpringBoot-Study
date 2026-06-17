package com.example.demo.Handler;

import org.springframework.web.bind.annotation.ResponseBody;

public class RequestCustomHandler {

    public String helloWorld(){
        System.out.println("[HANDLER] RequestCustomHandler's helloworld invoke..");
        return "memo/add";
    }
    public String helloWorld2(){
        System.out.println("[HANDLER] RequestCustomHandler's helloworld2 invoke..");
        return "HELLOWORLD";
    }
}
