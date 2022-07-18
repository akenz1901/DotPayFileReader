package com.dot.dotpay.service;

import com.dot.dotpay.data.dto.LogRequest;

public interface BlockedIpAddressService {
    void blockIpAddresses(LogRequest logRequest);
}
