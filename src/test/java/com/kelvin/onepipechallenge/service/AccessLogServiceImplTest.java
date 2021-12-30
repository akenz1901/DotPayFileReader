package com.kelvin.onepipechallenge.service;

import com.kelvin.onepipechallenge.data.model.Log;
import com.kelvin.onepipechallenge.data.repository.AccessLogsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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

}