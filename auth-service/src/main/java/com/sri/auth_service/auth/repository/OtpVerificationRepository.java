package com.sri.auth_service.auth.repository;

import com.sri.auth_service.auth.entity.OtpVerification;
import com.sri.auth_service.auth.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findByUserIdAndPurpose(Long userId, OtpPurpose purpose);

    void deleteByUserIdAndPurpose(Long userId, OtpPurpose purpose);
}
