package com.sri.web_gateway.integration.auth.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OtpTriggerResponse {

    private String message;
    private LocalDateTime otpExpiresAt;
}
