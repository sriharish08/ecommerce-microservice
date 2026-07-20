package com.sri.auth_service.auth.service.impl;

import com.sri.auth_service.auth.dto.request.*;
import com.sri.auth_service.auth.dto.response.*;
import com.sri.auth_service.auth.entity.User;
import com.sri.auth_service.auth.enums.OtpPurpose;
import com.sri.auth_service.auth.enums.Role;
import com.sri.auth_service.auth.exception.InvalidCredentialException;
import com.sri.auth_service.auth.exception.UserAlreadyExistsException;
import com.sri.auth_service.auth.exception.UserNotFoundException;
import com.sri.auth_service.auth.producer.NotificationProducer;
import com.sri.auth_service.auth.repository.UserRepository;
import com.sri.auth_service.security.JwtService;
import com.sri.auth_service.auth.service.AuthService;
import com.sri.auth_service.auth.service.OtpIssueResult;
import com.sri.auth_service.auth.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final NotificationProducer notificationProducer;

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
                .enabled(true) // Later change to false when registration OTP is enabled
                .build();

        user = userRepository.save(user);

        // Future Flow
        /*
        OtpIssueResult result = otpService.issueOtp(user, OtpPurpose.REGISTRATION);

        if (!result.reused()) {
            notificationProducer.sendOtp(
                    user,
                    result.otpVerification().getOtp(),
                    OtpPurpose.REGISTRATION
            );
        }

        return AuthResponse.builder()
                .message("Registration OTP sent successfully.")
                .build();
        */

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = getUserByEmail(request.getEmail());

        if (!user.isEnabled()) {
            throw new InvalidCredentialException("Account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialException("Invalid email or password");
        }

        OtpIssueResult result = otpService.issueOtp(user, OtpPurpose.LOGIN);

        if (!result.reused()) {

            log.info("Issued new LOGIN OTP for userId={}, publishing notification", user.getId());

            notificationProducer.sendOtp(
                    user,
                    result.otpVerification().getOtp(),
                    OtpPurpose.LOGIN
            );

        } else {

            log.info("Reusing existing valid LOGIN OTP for userId={}, skipping notification", user.getId());
        }

        String message = result.reused()
                ? "Please use your previous OTP. It is still valid."
                : "OTP sent successfully.";

        return LoginResponse.builder()
                .message(message)
                .otpSent(!result.reused())
                .otpExpiresAt(result.otpVerification().getExpiresAt())
                .build();
    }

    @Override
    public AuthResponse verifyOtp(OtpVerifyRequest request) {

        User user = getUserByEmail(request.getEmail());

        otpService.verifyOtp(user, request.getOtp(), OtpPurpose.LOGIN);

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public OtpTriggerResponse triggerOtp(OtpTriggerRequest request) {

        User user = getUserByEmail(request.getEmail());

        OtpIssueResult result =
                otpService.issueOtp(user, request.getPurpose());

        if (!result.reused()) {

            log.info("Issued new {} OTP for userId={}, publishing notification",
                    request.getPurpose(), user.getId());

            notificationProducer.sendOtp(
                    user,
                    result.otpVerification().getOtp(),
                    request.getPurpose()
            );

        } else {

            log.info("Reusing existing valid {} OTP for userId={}, skipping notification",
                    request.getPurpose(), user.getId());
        }

        return buildOtpTriggerResponse(result);
    }

    @Override
    public OtpTriggerResponse forgotPassword(ForgotPasswordRequest request) {

        User user = getUserByEmail(request.getEmail());

        OtpIssueResult result =
                otpService.issueOtp(user, OtpPurpose.PASSWORD_RESET);

        if (!result.reused()) {

            log.info("Issued new PASSWORD_RESET OTP for userId={}, publishing notification", user.getId());

            notificationProducer.sendOtp(
                    user,
                    result.otpVerification().getOtp(),
                    OtpPurpose.PASSWORD_RESET
            );

        } else {

            log.info("Reusing existing valid PASSWORD_RESET OTP for userId={}, skipping notification", user.getId());
        }

        return buildOtpTriggerResponse(result);
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {

        User user = getUserByEmail(request.getEmail());

        otpService.verifyOtp(
                user,
                request.getOtp(),
                OtpPurpose.PASSWORD_RESET
        );

        user.setPasswordHash(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        otpService.invalidateOtp(
                user,
                OtpPurpose.PASSWORD_RESET
        );

        return ResetPasswordResponse.builder()
                .message("Password reset successful. Please login with your new password.")
                .build();
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email));
    }

    private OtpTriggerResponse buildOtpTriggerResponse(OtpIssueResult result) {

        String message = result.reused()
                ? "Please use your previous OTP. It is still valid."
                : "OTP sent successfully.";

        return OtpTriggerResponse.builder()
                .message(message)
                .otpExpiresAt(result.otpVerification().getExpiresAt())
                .build();
    }
}