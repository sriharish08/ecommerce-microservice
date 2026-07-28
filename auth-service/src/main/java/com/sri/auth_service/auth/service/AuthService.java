package com.sri.auth_service.auth.service;

import com.sri.auth_service.auth.dto.request.ForgotPasswordRequest;
import com.sri.auth_service.auth.dto.request.LoginRequest;
import com.sri.auth_service.auth.dto.request.OtpTriggerRequest;
import com.sri.auth_service.auth.dto.request.OtpVerifyRequest;
import com.sri.auth_service.auth.dto.request.RegisterRequest;
import com.sri.auth_service.auth.dto.request.ResetPasswordRequest;
import com.sri.auth_service.auth.dto.response.AuthResponse;
import com.sri.auth_service.auth.dto.response.LoginResponse;
import com.sri.auth_service.auth.dto.response.OtpTriggerResponse;
import com.sri.auth_service.auth.dto.response.ResetPasswordResponse;

public interface AuthService {
    OtpTriggerResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    AuthResponse verifyOtp(OtpVerifyRequest request);

    OtpTriggerResponse triggerOtp(OtpTriggerRequest request);

    OtpTriggerResponse forgotPassword(ForgotPasswordRequest request);

    ResetPasswordResponse resetPassword(ResetPasswordRequest request);
}
