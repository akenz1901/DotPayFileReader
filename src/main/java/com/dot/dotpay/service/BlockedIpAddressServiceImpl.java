package com.dot.dotpay.service;

import com.dot.dotpay.data.model.BlockedIP;
import com.dot.dotpay.data.model.UserAccessLog;
import com.dot.dotpay.data.repository.BlockedIpRepository;
import com.dot.dotpay.data.dto.LogRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class BlockedIpAddressServiceImpl implements BlockedIpAddressService{

    @Autowired
    private UserAccessLogService userAccessLogService;

    @Autowired
    private BlockedIpRepository blockedIpRepository;

    @Override
    public void blockIpAddresses(LogRequest logRequest) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(logRequest.getStart(), format);
        boolean isHourlyDuration = logRequest.getDuration().name().equalsIgnoreCase("Hourly");

        LocalDateTime end = isHourlyDuration ?
                start.plus(1, ChronoUnit.HOURS)
                : start.plus(1, ChronoUnit.DAYS);

        List<UserAccessLog> userAccessLogList = userAccessLogService.findIpNumbersThatMadeRequestByStartDateDurationAndThreshold(logRequest);

        if(userAccessLogList.isEmpty()){
            log.info("Zero IP address exceeded limit");
            return;
        }

        // Creating BlockedIP entity from the data collected from userAccessLogCollection.

        userAccessLogList
                .forEach(userAccessLog -> {
                    System.out.println(userAccessLog.getIp());
                    BlockedIP blockedIp = new BlockedIP();
                    blockedIp.setIp(userAccessLog.getIp());
                    blockedIp.setRequestNumber(userAccessLog.getId());
                    blockedIp.setComment("Exceeded " + logRequest.getDuration() + " limit of " + logRequest.getLimit());

                    blockedIpRepository.save(blockedIp);
                });

        log.info("Successfully fetched blocked IP addresses");
    }
}
