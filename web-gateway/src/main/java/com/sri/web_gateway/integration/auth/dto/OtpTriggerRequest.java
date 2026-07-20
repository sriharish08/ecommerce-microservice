package com.sri.web_gateway.integration.auth.dto;

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
