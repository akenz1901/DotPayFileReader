package com.kelvin.onepipechallenge.controller;

import com.kelvin.onepipechallenge.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class AccessLogController {
    @Autowired
    private AccessLogService accessLogService;
    @PostMapping("/load-file")
    public ResponseEntity<?>uploadAccessLog(@RequestParam(value = "file", required = false) MultipartFile file){
        if(file == null){
            File file1 = new File("src/main/resources/access.log");
            try {
                accessLogService.collectAndStoreLogsIntoDataBase(file1);
                return ResponseEntity.ok().body("File uploaded successfully");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return ResponseEntity.ok().body("Please Load a File");
            }
        }
        File file2 = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            accessLogService.collectAndStoreLogsIntoDataBase(file2);
            return ResponseEntity.ok().body("File uploaded Successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Please Load a file");
        }
    }


}
