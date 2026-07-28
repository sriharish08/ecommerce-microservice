package com.sri.web_gateway.integration.auth.client;

import com.sri.web_gateway.common.response.Response;
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
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "auth-service",
        url = "${feign.client.auth-service}"
)
public interface AuthFeignClient {

    @PostMapping("/register")
    Response<OtpTriggerResponse> register(@RequestBody RegisterRequest request);

    @PostMapping("/login")
    Response<LoginResponse> login(@RequestBody LoginRequest request);

    @PostMapping("/otp/verify-otp")
    Response<AuthResponse> verifyOtp(@RequestBody OtpVerifyRequest request);

    @PostMapping("/otp/trigger-otp")
    Response<OtpTriggerResponse> triggerOtp(@RequestBody OtpTriggerRequest request);

    @PostMapping("/password/forgot-password")
    Response<OtpTriggerResponse> forgotPassword(@RequestBody ForgotPasswordRequest request);

    @PostMapping("/password/reset-password")
    Response<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest request);
}
