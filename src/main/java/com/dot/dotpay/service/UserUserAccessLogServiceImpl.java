package com.dot.dotpay.service;

import com.dot.dotpay.data.model.UserAccessLog;
import com.dot.dotpay.data.repository.AccessLogsRepository;
import com.dot.dotpay.exception.ApplicationException;
import com.dot.dotpay.data.dto.LogRequest;
import com.dot.dotpay.data.model.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class UserUserAccessLogServiceImpl implements UserAccessLogService {

    @Autowired
    AccessLogsRepository accessLogRepository;


    @Override
    public List<UserAccessLog> saveFiles(MultipartFile file) throws Exception {
        List<UserAccessLog> userAccessLogs = parseCSVFile(file);
        userAccessLogs = accessLogRepository.saveAll(userAccessLogs);
        return userAccessLogs;
    }


    @Override
    public List<UserAccessLog> saveFiles(File file) throws Exception {
        List<UserAccessLog> userAccessLogs = parseCSVFile(file);
        userAccessLogs = accessLogRepository.saveAll(userAccessLogs);
        return userAccessLogs;
    }

    private List<UserAccessLog> parseCSVFile(final MultipartFile file) throws Exception {
        final List<UserAccessLog> userAccessLogs = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split("\\|");
                    final UserAccessLog newUserAccessLog = new UserAccessLog();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                    newUserAccessLog.setStartDate(LocalDateTime.parse(data[0], formatter));
                    newUserAccessLog.setIp(data[1]);
                    newUserAccessLog.setMethod(data[2]);
                    newUserAccessLog.setCode(data[3]);
                    newUserAccessLog.setClient(data[4]);
                    userAccessLogs.add(newUserAccessLog);
                }
                return userAccessLogs;
            }
        } catch (final IOException e) {
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }

    private List<UserAccessLog> parseCSVFile(final File file) throws Exception {
        final List<UserAccessLog> userAccessLogs = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split("\\|");
                    final UserAccessLog newUserAccessLog = new UserAccessLog();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                    newUserAccessLog.setStartDate(LocalDateTime.parse(data[0], formatter));
                    newUserAccessLog.setIp(data[1]);
                    newUserAccessLog.setMethod(data[2]);
                    newUserAccessLog.setCode(data[3]);
                    newUserAccessLog.setClient(data[4]);
                    userAccessLogs.add(newUserAccessLog);
                }
                return userAccessLogs;
            }
        } catch (final IOException e) {
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }


    @Override
    public List<UserAccessLog> findRequestsMadeByAGivenIpNumber(String ipNumber) {
        String regex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)(\\.(?!$)|$)){4}$";
        if(ipNumber.matches(regex)) {
            return accessLogRepository.findRequestMadeByAnIpNumber(ipNumber);
        }else {
            throw new ApplicationException(ipNumber + " is an invalid Ip address");
        }
    }


    @Override
    public List<UserAccessLog> findIpNumbersThatMadeRequestByStartDateDurationAndThreshold(LogRequest logRequest) {
        LocalDateTime date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
            date = LocalDateTime.parse(logRequest.getStart(), formatter);
        } catch(DateTimeParseException ex){
            log.error("Invalid date --> {}", logRequest.getStart());
            throw new ApplicationException("The date " + logRequest.getStart() + " cannot be parsed");
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
}
