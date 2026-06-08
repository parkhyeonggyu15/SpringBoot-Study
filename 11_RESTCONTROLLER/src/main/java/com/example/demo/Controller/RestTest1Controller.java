package com.example.demo.Controller;

import com.example.demo.Domain.Common.Dtos.MemoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/rest")
public class RestTest1Controller {
    @GetMapping(value="/getText",produces = MediaType.TEXT_PLAIN_VALUE)
    public String t1(){
        log.info("GET /rest/getText");
        return "HELLOWORLD";
    }
    @GetMapping(value="/getJson",produces = MediaType.APPLICATION_JSON_VALUE)
    public MemoDTO t2(){
        log.info("GET /rest/getJson");
        return new MemoDTO(1L,"title1","text1","a@a.com", LocalDateTime.now());
    }
    @GetMapping(value="/getXml",produces = MediaType.APPLICATION_XML_VALUE)
    public MemoDTO t3(){
        log.info("GET /rest/getXml");
        return new MemoDTO(1L,"title1","text1","a@a.com", LocalDateTime.now());
    }

    @GetMapping(value="/getListJson",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MemoDTO> t4(){
        log.info("GET /rest/getListJson");
        List<MemoDTO> list = new ArrayList<>();
        for(long i=0;i<10;i++){
            list.add(new MemoDTO(i,"t"+i,"t"+i,"w"+i,LocalDateTime.now()));
        }
        return list;
    }
    @GetMapping(value="/getListXml",produces = MediaType.APPLICATION_XML_VALUE)
    public List<MemoDTO> t5(){
        log.info("GET /rest/getListXml");
        List<MemoDTO> list = new ArrayList<>();
        for(long i=0;i<10;i++){
            list.add(new MemoDTO(i,"t"+i,"t"+i,"w"+i,LocalDateTime.now()));
        }
        return list;
    }

    @GetMapping(value="/getListJson2/{show}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MemoDTO>> t6(@PathVariable("show") boolean show){
        log.info("GET /rest/getListJson2..." + show);
        List<MemoDTO> list = new ArrayList<>();
        for(long i=0;i<10;i++){
            list.add(new MemoDTO(i,"t"+i,"t"+i,"w"+i,LocalDateTime.now()));
        }
        if(show)
//            return new ResponseEntity<>(list, HttpStatus.OK); // status : 200
            return ResponseEntity.status(HttpStatus.OK).body(list);
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_GATEWAY); // status : 5xx
    }

}


