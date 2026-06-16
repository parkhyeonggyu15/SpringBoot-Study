package com.example.demo.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("/file")
public class FileUpDownloadController {

    private String ROOT_PATH = "c:";        //LINUX OS : '/'
    private String UPLOAD_PATH = "upload";

    @GetMapping("/upload")
    public void upload(){
        log.info("GET /file/upload...");
    }
    @PostMapping("/upload")
    @ResponseBody
    public void upload_post(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        log.info("Post /file/upload..." + file);
        System.out.println("FILE NAME : " + file.getOriginalFilename());
        System.out.println("FILE SIZE : " + file.getSize()+" Byte");
        String filename = file.getOriginalFilename();

        String uploadPath = ROOT_PATH
                + File.separator //구분자 '\' , '/'
                + UPLOAD_PATH
                + File.separator; // 'c://upload//'
        //업로드 폴더 부재시 생성
        File dir = new File(uploadPath);
        if(!dir.exists())
            dir.mkdirs();
        //파일 업로드(단일파일)
        File fileObject = new File(uploadPath,filename);
        file.transferTo(fileObject);
    }

}
