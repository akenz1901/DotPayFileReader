package com.kelvin.onepipechallenge.service;

import com.kelvin.onepipechallenge.data.dto.LogRequest;
import com.kelvin.onepipechallenge.data.model.Duration;
import com.kelvin.onepipechallenge.data.model.Log;
import com.kelvin.onepipechallenge.data.repository.AccessLogsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class AccessLogServiceImplTest {

    @Autowired
    AccessLogService accessLogService;

    @Autowired
    AccessLogsRepository accessLogsRep;

    @BeforeEach
    void setUp() {
        accessLogsRep.deleteAll();
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    @DisplayName("Read from the access log file")
    void testThatFilesCanBeReadAndDeleted() throws FileNotFoundException {
        String path = "src/main/resources/access.log";
        File file = new File(path);
        List<Log> accessLogs = accessLogService.collectLogsFromAccessLogFile(file);
        assertThat(accessLogs.size()).isEqualTo(116484);
        assertThat(accessLogs).isNotEmpty();
    }
    @Test
    @DisplayName("Read and Save The Access Logs Content into MySql Data Base")
    void testThatLogsCanBeReadAndSaveToTheMySqlDataBase() throws FileNotFoundException {
        String path = "src/main/resources/access.log";
        File file = new File(path);
        List<Log> accessLogs = accessLogService.collectAndStoreLogsIntoDataBase(file);
        assertThat(accessLogs.size()).isEqualTo(116484);
        assertThat(accessLogs).isNotEmpty();
    }
    @Test
    @DisplayName("Find Requests made by a given IP Number")
    void testToFindAllRequestMadeByAGivenIpNumber() throws FileNotFoundException {
        String path = "src/main/resources/access.log";
        File file = new File(path);
        accessLogService.collectAndStoreLogsIntoDataBase(file);
        String ipNumber = "192.168.102.136";
        List<Log> totalRequest = accessLogService.findRequestsMadeByAGivenIpNumber(ipNumber);
        assertThat(totalRequest.size()).isNotEqualTo(0);
        assertThat(totalRequest).isNotEmpty();
    }
    @Test
    @DisplayName("Find Ip Numbers Between Start Date and End Dates Using its Threshold")
    void testToFindIpNumbersWithItsThresholdBetweenStartDateAndEndDates() throws FileNotFoundException {
        String path = "src/main/resources/access.log";
        File file = new File(path);
        accessLogService.collectAndStoreLogsIntoDataBase(file);
        LogRequest logRequest = new LogRequest();
        logRequest.setStartDate("2017-01-01.13:00:00");
        logRequest.setDuration(Duration.hourly);
        logRequest.setThreshold(100L);
        List<String>ipNumbers = accessLogService.findIpNumbersThatMadeRequestByStartDateDurationAndThreshold(logRequest);
        log.info("ipNumbers after requesting -> {}", ipNumbers);
         assertThat(ipNumbers).isNotEmpty();
        }
    }