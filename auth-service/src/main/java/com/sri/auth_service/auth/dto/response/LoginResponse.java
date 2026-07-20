package com.sri.auth_service.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class LoginResponse {

    private String message;

    private boolean otpSent;

    private LocalDateTime otpExpiresAt;

}