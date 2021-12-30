package com.kelvin.onepipechallenge.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kelvin.onepipechallenge.data.model.Duration;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogRequest {
    private Long threshold;
    private String startDate;
    private Duration duration;
}
