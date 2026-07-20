package com.sri.auth_service.auth.service;

import com.sri.auth_service.auth.entity.User;
import com.sri.auth_service.auth.enums.OtpPurpose;

public interface OtpService {

    OtpIssueResult issueOtp(User user, OtpPurpose purpose);

    void verifyOtp(User user, String otp, OtpPurpose purpose);

    void invalidateOtp(User user, OtpPurpose purpose);
}
