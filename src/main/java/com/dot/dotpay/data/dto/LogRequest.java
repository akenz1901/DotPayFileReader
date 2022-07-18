package com.dot.dotpay.data.dto;

import com.dot.dotpay.data.model.Duration;
import lombok.Data;

@Data
public class LogRequest {
    private Long threshold;
    private String start;
    private Duration duration;
    private int limit;
}
