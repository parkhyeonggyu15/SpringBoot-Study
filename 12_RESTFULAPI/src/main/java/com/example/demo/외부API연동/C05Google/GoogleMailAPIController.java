package com.example.demo.외부API연동.C05Google;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/google/mail")
public class GoogleMailAPIController {

    @Autowired
    JavaMailSender javaMailSender;

    @GetMapping("/{recv}/{text}")
    @ResponseBody
    public void send_mail(
            @PathVariable String recv,
            @PathVariable String text
    ){
        log.info("GET /google/mail....{},{}",recv,text);
        log.info("javaMailSender....{}",javaMailSender);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recv);
        message.setSubject("[WEB발신] 메일 테스트 입니다.!!!");
        message.setText(text);

        javaMailSender.send(message);
    }
}


