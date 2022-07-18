package com.dot.dotpay.controller;

import com.dot.dotpay.data.model.UserAccessLog;
import com.dot.dotpay.exception.ApplicationException;
import com.dot.dotpay.service.UserAccessLogService;
import com.dot.dotpay.data.dto.IpInfo;
import com.dot.dotpay.data.dto.LogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AccessLogController {

    private final UserAccessLogService userAccessLogService;

    @PostMapping(value = "/load-file", produces = "application/json")
    public ResponseEntity<?> saveUsers(@RequestParam(value = "files", required=false) MultipartFile[] files) throws Exception {
        if(files == null){
            userAccessLogService.saveFiles(ResourceUtils.getFile("classpath:access.log"));
        }else {
            for (MultipartFile file : files) {
                userAccessLogService.saveFiles(file);
            }
        }
        return ResponseEntity.ok().body("File uploaded successfully");
    }

    @GetMapping("/listIp")
    public ResponseEntity<?> getListOfIp(@RequestBody LogRequest request){
        try{

            List<UserAccessLog> response = userAccessLogService.findIpNumbersThatMadeRequestByStartDateDurationAndThreshold(request);
            return ResponseEntity.ok().body(response);
        }catch(ApplicationException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/ipRequest")
    public ResponseEntity<?> getIpRequest(@RequestBody IpInfo ipNumber){
        try{
            List<UserAccessLog> response = userAccessLogService.findRequestsMadeByAGivenIpNumber(ipNumber.getIpNumber());
            return ResponseEntity.ok().body(response);
        }catch(ApplicationException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
