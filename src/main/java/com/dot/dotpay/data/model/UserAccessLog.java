package com.dot.dotpay.data.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "user_access_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime startDate;
    private String ip;
    private String method;
    private String code;
    private String client;
    private String requestCount;
}
