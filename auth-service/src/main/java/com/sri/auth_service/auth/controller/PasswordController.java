package com.sri.auth_service.auth.controller;

import com.sri.auth_service.auth.dto.request.ForgotPasswordRequest;
import com.sri.auth_service.auth.dto.request.ResetPasswordRequest;
import com.sri.auth_service.auth.dto.response.OtpTriggerResponse;
import com.sri.auth_service.auth.dto.response.ResetPasswordResponse;
import com.sri.auth_service.auth.service.AuthService;
import com.sri.auth_service.common.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/password")
@RequiredArgsConstructor
public class PasswordController {

    private final AuthService authService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Response<OtpTriggerResponse>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        Response<OtpTriggerResponse> response = Response.ok();
        response.setPayload(authService.forgotPassword(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response<ResetPasswordResponse>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        Response<ResetPasswordResponse> response = Response.ok();
        response.setPayload(authService.resetPassword(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
