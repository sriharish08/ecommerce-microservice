package com.sri.auth_service.auth.controller;

import com.sri.auth_service.auth.dto.request.OtpTriggerRequest;
import com.sri.auth_service.auth.dto.request.OtpVerifyRequest;
import com.sri.auth_service.auth.dto.response.AuthResponse;
import com.sri.auth_service.auth.dto.response.OtpTriggerResponse;
import com.sri.auth_service.auth.service.AuthService;
import com.sri.auth_service.common.response.Response;
import com.sri.auth_service.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/otp")
@RequiredArgsConstructor
public class OtpController {

    private final AuthService authService;

    @PostMapping("/verify-otp")
    public ResponseEntity<Response<AuthResponse>> verifyOtp(
            @Valid @RequestBody OtpVerifyRequest request) {

        return ResponseBuilder.success(authService.verifyOtp(request));
    }

    @PostMapping("/trigger-otp")
    public ResponseEntity<Response<OtpTriggerResponse>> triggerOtp(
            @Valid @RequestBody OtpTriggerRequest request) {

        return ResponseBuilder.success(authService.triggerOtp(request));
    }
}
