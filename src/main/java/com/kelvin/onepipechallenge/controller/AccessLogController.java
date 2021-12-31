package com.kelvin.onepipechallenge.controller;

import com.kelvin.onepipechallenge.data.dto.IpInfo;
import com.kelvin.onepipechallenge.data.dto.LogRequest;
import com.kelvin.onepipechallenge.data.model.Log;
import com.kelvin.onepipechallenge.exception.ApplicationException;
import com.kelvin.onepipechallenge.service.AccessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccessLogController {

    private final AccessLogService accessLogService;

    @PostMapping(value = "/load-file", produces = "application/json")
    public ResponseEntity<?> saveUsers(@RequestParam(value = "files", required=false) MultipartFile[] files) throws Exception {
        if(files == null){
            accessLogService.saveFiles(ResourceUtils.getFile("classpath:access.log"));
        }else {
            for (MultipartFile file : files) {
                accessLogService.saveFiles(file);
            }
        }
        return ResponseEntity.ok().body("File uploaded successfully");
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
