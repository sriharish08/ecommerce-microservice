package com.sri.auth_service.auth.dto.request;

import com.sri.auth_service.auth.enums.OtpPurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OtpTriggerRequest {

    @Email
    @NotBlank
    private String email;

    @NotNull
    private OtpPurpose purpose;
}
