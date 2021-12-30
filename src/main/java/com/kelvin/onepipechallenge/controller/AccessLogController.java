package com.kelvin.onepipechallenge.controller;

import com.kelvin.onepipechallenge.data.dto.IpInfo;
import com.kelvin.onepipechallenge.data.dto.LogRequest;
import com.kelvin.onepipechallenge.data.model.Log;
import com.kelvin.onepipechallenge.exception.ApplicationException;
import com.kelvin.onepipechallenge.service.AccessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccessLogController {

    private final AccessLogService accessLogService;

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

    @GetMapping("/listIp")
    public ResponseEntity<?> getListOfIp(@RequestBody LogRequest request){
        try{

            List<String> response = accessLogService.findIpNumbersThatMadeRequestByStartDateDurationAndThreshold(request);
            return ResponseEntity.ok().body(response);
        }catch(ApplicationException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/ipRequest")
    public ResponseEntity<?> getIpRequest(@RequestBody IpInfo ipNumber){
        try{
            List<Log> response = accessLogService.findRequestsMadeByAGivenIpNumber(ipNumber.getIpNumber());
            return ResponseEntity.ok().body(response);
        }catch(ApplicationException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


}
