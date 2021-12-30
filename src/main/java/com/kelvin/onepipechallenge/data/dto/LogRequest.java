package com.kelvin.onepipechallenge.data.dto;

import com.kelvin.onepipechallenge.data.model.Duration;
import lombok.Data;

@Data
public class LogRequest {
    private Long threshold;
    private String startDate;
    private Duration duration;
}
