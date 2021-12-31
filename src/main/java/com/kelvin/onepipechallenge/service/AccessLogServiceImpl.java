package com.kelvin.onepipechallenge.service;

import com.kelvin.onepipechallenge.data.dto.LogRequest;
import com.kelvin.onepipechallenge.data.model.Duration;
import com.kelvin.onepipechallenge.data.model.Log;
import com.kelvin.onepipechallenge.data.repository.AccessLogsRepository;
import com.kelvin.onepipechallenge.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
public class AccessLogServiceImpl implements AccessLogService{

    @Autowired
    AccessLogsRepository accessLogRepository;

    @Override
    public List<Log> findRequestsMadeByAGivenIpNumber(String ipNumber) {
        String regex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)(\\.(?!$)|$)){4}$";
        if(ipNumber.matches(regex)) {
            return accessLogRepository.findRequestMadeByAnIpNumber(ipNumber);
        }else {
            throw new ApplicationException(ipNumber + " is an invalid Ip address");
        }
    }

    @Override
    public List<String> findIpNumbersThatMadeRequestByStartDateDurationAndThreshold(LogRequest logRequest) {
        LocalDateTime date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
            date = LocalDateTime.parse(logRequest.getStartDate(), formatter);
        } catch(DateTimeParseException ex){
            log.error("Invalid date --> {}", logRequest.getStartDate());
            throw new ApplicationException("The date " + logRequest.getStartDate() + " cannot be parsed");
        }
        LocalDateTime dateTime;
        if(logRequest.getDuration().equals(Duration.hourly)){
            dateTime = date.plusHours(1);
        }else{
            if(logRequest.getDuration().equals(Duration.daily)){
                dateTime = date.plusHours(24);
            }else{
                throw new ApplicationException("Duration not allowed");
            }
        }
        return accessLogRepository.findByStartDateIsBetweenAndGreaterThanThreshold(date, dateTime, logRequest.getThreshold());
    }

    @Async
    @Override
    public CompletableFuture<List<Log>> saveFiles(MultipartFile file) throws Exception {
        List<Log> logs = parseCSVFile(file);
        logs = accessLogRepository.saveAll(logs);
        return CompletableFuture.completedFuture(logs);
    }

    @Async
    @Override
    public CompletableFuture<List<Log>> saveFiles(File file) throws Exception {
        List<Log> logs = parseCSVFile(file);
        logs = accessLogRepository.saveAll(logs);
        return CompletableFuture.completedFuture(logs);
    }

    private List<Log> parseCSVFile(final MultipartFile file) throws Exception {
        final List<Log> logs = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split("\\|");
                    final Log newLog = new Log();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                    newLog.setStartDate(LocalDateTime.parse(data[0], formatter));
                    newLog.setIp(data[1]);
                    newLog.setMethod(data[2]);
                    newLog.setCode(data[3]);
                    newLog.setClient(data[4]);
                    logs.add(newLog);
                }
                return logs;
            }
        } catch (final IOException e) {
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }

    private List<Log> parseCSVFile(final File file) throws Exception {
        final List<Log> logs = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split("\\|");
                    final Log newLog = new Log();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                    newLog.setStartDate(LocalDateTime.parse(data[0], formatter));
                    newLog.setIp(data[1]);
                    newLog.setMethod(data[2]);
                    newLog.setCode(data[3]);
                    newLog.setClient(data[4]);
                    logs.add(newLog);
                }
                return logs;
            }
        } catch (final IOException e) {
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }
}
