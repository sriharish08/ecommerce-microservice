package com.sri.auth_service.auth.service.impl;


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
import com.sri.auth_service.auth.entity.User;
import com.sri.auth_service.auth.enums.OtpPurpose;
import com.sri.auth_service.auth.enums.Role;
import com.sri.auth_service.auth.exception.InvalidCredentialException;
import com.sri.auth_service.auth.exception.UserAlreadyExistsException;
import com.sri.auth_service.auth.exception.UserNotFoundException;
import com.sri.auth_service.auth.repository.UserRepository;
import com.sri.auth_service.auth.service.OtpIssueResult;
import com.sri.auth_service.auth.service.OtpService;
import com.sri.auth_service.security.JwtService;
import com.sri.auth_service.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .message("Registration successful")
                .token(token)
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + request.getEmail()));

        if (!user.isEnabled()) {
            throw new InvalidCredentialException("Account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialException("Invalid email or password");
        }

        OtpIssueResult result = otpService.issueOtp(user, OtpPurpose.LOGIN);

        String message = result.reused()
                ? "An OTP was already sent to you earlier today and is still valid. Please use the previous OTP."
                : "OTP sent successfully. It is valid for 24 hours.";

        return LoginResponse.builder()
                .message(message)
                .otpExpiresAt(result.otpVerification().getExpiresAt())
                .build();
    }

    @Override
    public AuthResponse verifyOtp(OtpVerifyRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + request.getEmail()));

        otpService.verifyOtp(user, request.getOtp(), OtpPurpose.LOGIN);

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .message("OTP verified successfully")
                .token(token)
                .build();
    }

    @Override
    public OtpTriggerResponse triggerOtp(OtpTriggerRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + request.getEmail()));

        OtpIssueResult result = otpService.issueOtp(user, request.getPurpose());

        return buildOtpTriggerResponse(result);
    }

    @Override
    public OtpTriggerResponse forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + request.getEmail()));

        OtpIssueResult result = otpService.issueOtp(user, OtpPurpose.PASSWORD_RESET);

        return buildOtpTriggerResponse(result);
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + request.getEmail()));

        otpService.verifyOtp(user, request.getOtp(), OtpPurpose.PASSWORD_RESET);

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        otpService.invalidateOtp(user, OtpPurpose.PASSWORD_RESET);

        return ResetPasswordResponse.builder()
                .message("Password reset successful. Please login with your new password.")
                .build();
    }

    private OtpTriggerResponse buildOtpTriggerResponse(OtpIssueResult result) {

        String message = result.reused()
                ? "An OTP was already sent to you earlier today and is still valid. Please use the previous OTP."
                : "OTP sent successfully. It is valid for 24 hours.";

        return OtpTriggerResponse.builder()
                .message(message)
                .otpExpiresAt(result.otpVerification().getExpiresAt())
                .build();
    }
}
