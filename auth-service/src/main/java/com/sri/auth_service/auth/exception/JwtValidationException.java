package com.sri.auth_service.auth.exception;


public class JwtValidationException extends RuntimeException {

    public JwtValidationException(String message) {
        super(message);
    }
}