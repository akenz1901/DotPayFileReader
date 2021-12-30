package com.kelvin.onepipechallenge.service;

import com.kelvin.onepipechallenge.data.dto.LogRequest;
import com.kelvin.onepipechallenge.data.model.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface AccessLogService {
    List<Log> collectLogsFromAccessLogFile(File file) throws FileNotFoundException;
    List<Log> collectAndStoreLogsIntoDataBase(File file) throws FileNotFoundException;
    List<Log> findRequestsMadeByAGivenIpNumber(String ipNumber);
    List<String> findIpNumbersThatMadeRequestByStartDateDurationAndThreshold(LogRequest logRequest);
}
