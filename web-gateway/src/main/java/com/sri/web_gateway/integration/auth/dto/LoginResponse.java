package com.sri.web_gateway.integration.auth.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponse {

    private String message;
    private boolean otpSent;
    private LocalDateTime otpExpiresAt;
}
