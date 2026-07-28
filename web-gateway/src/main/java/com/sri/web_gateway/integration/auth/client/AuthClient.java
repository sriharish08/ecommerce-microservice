package com.sri.web_gateway.integration.auth.client;

import com.sri.web_gateway.exception.ServiceUnavailableException;
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
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthClient {

    private static final Logger log = LoggerFactory.getLogger(AuthClient.class);

    private final AuthFeignClient authFeignClient;

    @CircuitBreaker(name = "authRegister", fallbackMethod = "registerFallback")
    public OtpTriggerResponse register(RegisterRequest request) {

        log.info("Calling Auth Service to register user email={}", request.getEmail());

        return authFeignClient.register(request).getPayload();
    }

    private OtpTriggerResponse registerFallback(RegisterRequest request, Throwable exception) {
        return fail(exception);
    }

    @CircuitBreaker(name = "authLogin", fallbackMethod = "loginFallback")
    public LoginResponse login(LoginRequest request) {

        log.info("Calling Auth Service to login user email={}", request.getEmail());

        return authFeignClient.login(request).getPayload();
    }

    private LoginResponse loginFallback(LoginRequest request, Throwable exception) {
        return fail(exception);
    }

    @CircuitBreaker(name = "authOtpVerify", fallbackMethod = "verifyOtpFallback")
    public AuthResponse verifyOtp(OtpVerifyRequest request) {

        log.info("Calling Auth Service to verify OTP for email={}", request.getEmail());

        return authFeignClient.verifyOtp(request).getPayload();
    }

    private AuthResponse verifyOtpFallback(OtpVerifyRequest request, Throwable exception) {
        return fail(exception);
    }

    @CircuitBreaker(name = "authOtpTrigger", fallbackMethod = "triggerOtpFallback")
    public OtpTriggerResponse triggerOtp(OtpTriggerRequest request) {

        log.info("Calling Auth Service to trigger OTP for email={} purpose={}", request.getEmail(), request.getPurpose());

        return authFeignClient.triggerOtp(request).getPayload();
    }

    private OtpTriggerResponse triggerOtpFallback(OtpTriggerRequest request, Throwable exception) {
        return fail(exception);
    }

    @CircuitBreaker(name = "authForgotPassword", fallbackMethod = "forgotPasswordFallback")
    public OtpTriggerResponse forgotPassword(ForgotPasswordRequest request) {

        log.info("Calling Auth Service to trigger password reset OTP for email={}", request.getEmail());

        return authFeignClient.forgotPassword(request).getPayload();
    }

    private OtpTriggerResponse forgotPasswordFallback(ForgotPasswordRequest request, Throwable exception) {
        return fail(exception);
    }

    @CircuitBreaker(name = "authResetPassword", fallbackMethod = "resetPasswordFallback")
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {

        log.info("Calling Auth Service to reset password for email={}", request.getEmail());

        return authFeignClient.resetPassword(request).getPayload();
    }

    private ResetPasswordResponse resetPasswordFallback(ResetPasswordRequest request, Throwable exception) {
        return fail(exception);
    }

    /**
     * A 4xx from auth-service (bad credentials, validation, etc.) is the caller's fault, not a
     * health signal for auth-service - it's rethrown as-is instead of being masked as 503.
     */
    private <T> T fail(Throwable exception) {

        if (exception instanceof FeignException.FeignClientException) {
            throw (FeignException) exception;
        }

        log.error("Auth Service call failed. Reason: {}", exception.getMessage());

        throw new ServiceUnavailableException("auth-service", "auth-service is temporarily unavailable, please try again later");
    }
}
