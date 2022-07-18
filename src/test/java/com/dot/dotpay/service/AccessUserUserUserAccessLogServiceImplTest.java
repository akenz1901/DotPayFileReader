package com.dot.dotpay.service;

import com.dot.dotpay.data.model.UserAccessLog;
import com.dot.dotpay.data.repository.AccessLogsRepository;
import com.dot.dotpay.data.dto.LogRequest;
import com.dot.dotpay.data.model.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@SpringBootTest
@Slf4j
class AccessUserUserUserAccessLogServiceImplTest {

    @Autowired
    UserAccessLogService userAccessLogService;

    @Autowired
    AccessLogsRepository accessLogsRep;

    @BeforeEach
    void setUp() {
        //accessLogsRep.deleteAll();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Read and Save The Access Logs Content into MySql Data Base")
    void testThatLogsCanBeReadAndSaveToTheMySqlDataBase() throws Exception {
        String path = "src/main/resources/access.log";
        File file = new File(path);
        List<UserAccessLog> accessUserAccessLogs = userAccessLogService.saveFiles(file);
        assertThat(accessUserAccessLogs.size()).isEqualTo(116484);
        assertThat(accessUserAccessLogs).isNotEmpty();
    }

    @Test
    @DisplayName("Find Requests made by a given IP Number")
    void testToFindAllRequestMadeByAGivenIpNumber() throws Exception {
        String ipNumber = "192.168.14.43";
        List<UserAccessLog> totalRequest = userAccessLogService.findRequestsMadeByAGivenIpNumber(ipNumber);
        assertThat(totalRequest.size()).isNotEqualTo(0);
        assertThat(totalRequest).isNotEmpty();
    }

    @Test
    @DisplayName("Find Ip Numbers Between Start Date and End Dates Using its Threshold")
    void testToFindIpNumbersWithItsThresholdBetweenStartDateAndEndDates() throws Exception {
        LogRequest logRequest = new LogRequest();
        logRequest.setStart("2017-01-01.13:00:00");
        logRequest.setDuration(Duration.hourly);
        logRequest.setThreshold(100L);
        List<UserAccessLog>ipNumbers = userAccessLogService.findIpNumbersThatMadeRequestByStartDateDurationAndThreshold(logRequest);
        log.info("ipNumbers after requesting -> {}", ipNumbers);
         assertThat(ipNumbers).isNotEmpty();
        }
    }