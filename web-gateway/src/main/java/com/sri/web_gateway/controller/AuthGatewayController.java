package com.sri.web_gateway.controller;

import com.sri.web_gateway.common.response.Response;
import com.sri.web_gateway.common.response.ResponseBuilder;
import com.sri.web_gateway.integration.auth.client.AuthClient;
import com.sri.web_gateway.integration.auth.dto.AuthResponse;
import com.sri.web_gateway.integration.auth.dto.ForgotPasswordRequest;
import com.sri.web_gateway.integration.auth.dto.LoginRequest;
import com.sri.web_gateway.integration.auth.dto.LoginResponse;
import com.sri.web_gateway.integration.auth.dto.OtpTriggerRequest;
import com.sri.web_gateway.integration.auth.dto.OtpTriggerResponse;
import com.sri.web_gateway.integration.auth.dto.OtpVerifyRequest;
import com.sri.web_gateway.integration.auth.dto.RegisterRequest;
import com.sri.web_gateway.integration.auth.dto.ResetPasswordRequest;
import com.sri.web_gateway.integration.auth.dto.ResetPasswordResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthGatewayController {

    private final AuthClient authClient;

    @PostMapping("/register")
    public ResponseEntity<Response<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseBuilder.success(authClient.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseBuilder.success(authClient.login(request));
    }

    @PostMapping("/otp/verify-otp")
    public ResponseEntity<Response<AuthResponse>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        return ResponseBuilder.success(authClient.verifyOtp(request));
    }

    @PostMapping("/otp/trigger-otp")
    public ResponseEntity<Response<OtpTriggerResponse>> triggerOtp(@Valid @RequestBody OtpTriggerRequest request) {
        return ResponseBuilder.success(authClient.triggerOtp(request));
    }

    @PostMapping("/password/forgot-password")
    public ResponseEntity<Response<OtpTriggerResponse>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseBuilder.success(authClient.forgotPassword(request));
    }

    @PostMapping("/password/reset-password")
    public ResponseEntity<Response<ResetPasswordResponse>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseBuilder.success(authClient.resetPassword(request));
    }
}
