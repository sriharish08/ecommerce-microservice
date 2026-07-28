package com.sri.auth_service.auth.service.impl;

import com.sri.auth_service.auth.entity.OtpVerification;
import com.sri.auth_service.auth.entity.User;
import com.sri.auth_service.auth.enums.OtpPurpose;
import com.sri.auth_service.auth.exception.OtpValidationException;
import com.sri.auth_service.auth.repository.OtpVerificationRepository;
import com.sri.auth_service.auth.service.OtpIssueResult;
import com.sri.auth_service.auth.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpServiceImpl.class);
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final long OTP_VALIDITY_DAYS = 1;

    private final OtpVerificationRepository otpVerificationRepository;

    @Override
    public OtpIssueResult issueOtp(User user, OtpPurpose purpose) {

        OtpVerification existing = otpVerificationRepository
                .findByUserIdAndPurpose(user.getId(), purpose)
                .orElse(null);

        if (existing != null && !existing.isExpired()) {
            log.info("Reusing existing OTP for user={} purpose={}, still valid until {}",
                    user.getEmail(), purpose, existing.getExpiresAt());
            return new OtpIssueResult(existing, true);
        }

        OtpVerification otpVerification = existing != null ? existing : OtpVerification.builder()
                .user(user)
                .purpose(purpose)
                .build();

        otpVerification.setOtp(generateOtp());
        otpVerification.setExpiresAt(LocalDateTime.now().plusDays(OTP_VALIDITY_DAYS));

        otpVerification = otpVerificationRepository.save(otpVerification);

        log.info("Generated new OTP {} for user={} purpose={}, valid until {}",
                otpVerification.getOtp(), user.getEmail(), purpose, otpVerification.getExpiresAt());

        return new OtpIssueResult(otpVerification, false);
    }

    @Override
    public void verifyOtp(User user, String otp, OtpPurpose purpose) {

        OtpVerification otpVerification = otpVerificationRepository
                .findByUserIdAndPurpose(user.getId(), purpose)
                .orElseThrow(() -> new OtpValidationException("No OTP found. Please login again to generate a new OTP."));

        if (otpVerification.isExpired()) {
            throw new OtpValidationException("OTP has expired. Please login again to generate a new OTP.");
        }

        if (!otpVerification.getOtp().equals(otp)) {
            throw new OtpValidationException("Invalid OTP.");
        }
    }

    @Override
    @Transactional
    public void invalidateOtp(User user, OtpPurpose purpose) {
        otpVerificationRepository.deleteByUserIdAndPurpose(user.getId(), purpose);
    }

    private String generateOtp() {
        return String.valueOf(100000 + RANDOM.nextInt(900000));
    }
}
