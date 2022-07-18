package com.dot.dotpay.service;

import com.dot.dotpay.data.dto.LogRequest;
import com.dot.dotpay.data.model.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;

@Service
@Slf4j
public class FileService {

    private final UserAccessLogService userUserAccessLogServices;
    private final BlockedIpAddressService blockedIPServices;
    private final ApplicationContext applicationContext;

    public FileService(UserAccessLogService userAccessLogService, BlockedIpAddressService blockedIPServices, ApplicationContext applicationContext) {
        this.userUserAccessLogServices = userAccessLogService;
        this.blockedIPServices = blockedIPServices;
        this.applicationContext = applicationContext;
    }

    public void SaveFIleToDB(String... args){
        Duration duration = null;
        int limit = 0;

        if(args[2].equalsIgnoreCase("Hourly")){
            duration = Duration.hourly;
            limit = 200;
        }else if(args[2].equalsIgnoreCase("Daily")){
            duration = Duration.daily;
            limit = 500;
        }else{
            throw new RuntimeException("Invalid duration. Duration must be either hourly or daily.");

        }

        try (InputStream is = new URL(args[0]).openStream()){
            userUserAccessLogServices.saveFiles((MultipartFile) is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        activateBlockIPRequest(duration, limit, args);
    }

    private void activateBlockIPRequest(Duration duration, int limit, String[] args) {
        LogRequest blockedIPRequest = new LogRequest();
        blockedIPRequest.setDuration(duration);
        blockedIPRequest.setLimit(limit);
        blockedIPRequest.setStart(args[1]);

        log.info("Preparing to fetch IP ....................................");
        blockedIPServices.blockIpAddresses(blockedIPRequest);
    }

}
