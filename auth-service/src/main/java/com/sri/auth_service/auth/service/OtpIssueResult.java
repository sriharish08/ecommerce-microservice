package com.sri.auth_service.auth.service;

import com.sri.auth_service.auth.entity.OtpVerification;

public record OtpIssueResult(OtpVerification otpVerification, boolean reused) {
}
