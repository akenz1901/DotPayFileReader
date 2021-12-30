package com.kelvin.onepipechallenge.service;

import com.kelvin.onepipechallenge.data.dto.LogRequest;
import com.kelvin.onepipechallenge.data.model.Duration;
import com.kelvin.onepipechallenge.data.model.Log;
import com.kelvin.onepipechallenge.data.repository.AccessLogsRepository;
import com.kelvin.onepipechallenge.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


@Slf4j
@Service
public class AccessLogServiceImpl implements AccessLogService{

    @Autowired
    AccessLogsRepository accessLogRepository;

    @Override
    public List<Log> collectLogsFromAccessLogFile(File file) throws FileNotFoundException {
        List<Log> logs = new ArrayList<>();
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            Scanner read = new Scanner(line);
            read.useDelimiter("\\|");
            while (read.hasNext()) {
                Log log = new Log();
                String date = read.next();
//                date = date.replace(" ", "T");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                log.setStartDate(LocalDateTime.parse(date, formatter));
                log.setIp(read.next());
                log.setMethod(read.next());
                log.setCode(read.next());
                log.setClient(read.next());
                logs.add(log);
            }
            read.close();
        }
        reader.close();
        return logs;
    }

    @Override
    public List<Log> collectAndStoreLogsIntoDataBase(File file) throws FileNotFoundException {
        List<Log>accessLogs = collectLogsFromAccessLogFile(file);
        return accessLogRepository.saveAll(accessLogs);
    }

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

//            date = logRequest.getStartDate();
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

}
