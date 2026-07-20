package com.sri.auth_service.common.advice;


import com.sri.auth_service.auth.exception.InvalidCredentialException;
import com.sri.auth_service.auth.exception.JwtValidationException;
import com.sri.auth_service.auth.exception.OtpValidationException;
import com.sri.auth_service.auth.exception.UserAlreadyExistsException;
import com.sri.auth_service.auth.exception.UserNotFoundException;
import com.sri.auth_service.common.response.Response;
import com.sri.auth_service.common.response.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response<Object>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<Response<Object>> handleInvalidCredential(InvalidCredentialException ex) {
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<Response<Object>> handleJwtValidation(JwtValidationException ex) {
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Response<Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OtpValidationException.class)
    public ResponseEntity<Response<Object>> handleOtpValidation(OtpValidationException ex) {
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseBuilder.error(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleException(Exception ex) {
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
