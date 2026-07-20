package com.sri.web_gateway.integration.auth.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private String token;
    private String message;
}
