package com.kelvin.onepipechallenge.service;

import com.kelvin.onepipechallenge.data.dto.LogRequest;
import com.kelvin.onepipechallenge.data.model.Log;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AccessLogService {
    List<Log> findRequestsMadeByAGivenIpNumber(String ipNumber);
    List<String> findIpNumbersThatMadeRequestByStartDateDurationAndThreshold(LogRequest logRequest);
    List<Log> saveFiles(MultipartFile file) throws Exception;
    List<Log> saveFiles(File file) throws Exception;
}
