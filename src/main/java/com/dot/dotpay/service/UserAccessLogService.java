package com.dot.dotpay.service;

import com.dot.dotpay.data.model.UserAccessLog;
import com.dot.dotpay.data.dto.LogRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface UserAccessLogService {
    List<UserAccessLog> findRequestsMadeByAGivenIpNumber(String ipNumber);
    List<UserAccessLog> findIpNumbersThatMadeRequestByStartDateDurationAndThreshold(LogRequest logRequest);
    List<UserAccessLog> saveFiles(MultipartFile file) throws Exception;
    List<UserAccessLog> saveFiles(File file) throws Exception;
}
