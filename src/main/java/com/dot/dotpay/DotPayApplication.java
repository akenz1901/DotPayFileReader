package com.dot.dotpay;

import com.dot.dotpay.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@SpringBootApplication
public class DotPayApplication implements CommandLineRunner {

    @Autowired
    private FileService fileService;

    public static void main(String[] args) {
        SpringApplication.run(DotPayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Fetching data from file .................................... ");


        fileService.SaveFIleToDB(args);
    }

}
