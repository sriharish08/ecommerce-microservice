package com.sri.auth_service.common.advice;


import com.sri.auth_service.auth.exception.InvalidCredentialException;
import com.sri.auth_service.auth.exception.JwtValidationException;
import com.sri.auth_service.auth.exception.OtpValidationException;
import com.sri.auth_service.auth.exception.UserAlreadyExistsException;
import com.sri.auth_service.auth.exception.UserNotFoundException;
import com.sri.auth_service.common.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response<Object>> handleUserNotFound(UserNotFoundException ex) {
        logger.error("UserNotFoundException : {}", ex.getMessage());
        Response<Object> response = Response.notFound();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<Response<Object>> handleInvalidCredential(InvalidCredentialException ex) {
        logger.error("InvalidCredentialException : {}", ex.getMessage());
        Response<Object> response = Response.unauthorized();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<Response<Object>> handleJwtValidation(JwtValidationException ex) {
        logger.error("JwtValidationException : {}", ex.getMessage());
        Response<Object> response = Response.unauthorized();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Response<Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        logger.error("UserAlreadyExistsException : {}", ex.getMessage());
        Response<Object> response = Response.conflict();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OtpValidationException.class)
    public ResponseEntity<Response<Object>> handleOtpValidation(OtpValidationException ex) {
        logger.error("OtpValidationException : {}", ex.getMessage());
        Response<Object> response = Response.badRequest();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        logger.error("DataIntegrityViolationException : {}", ex.getMessage());
        Response<Object> response = Response.conflict();
        response.addErrorMsgToResponse("Username or email already exists", ex);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Response<Object>> handleNoResourceFound(NoResourceFoundException ex) {
        Response<Object> response = Response.notFound();
        response.addErrorMsgToResponse("No such endpoint", ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        logger.error("ValidationException : {}", errors);
        Response<Object> response = Response.badRequest();
        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleException(Exception ex) {
        logger.error("Exception : {}", ex.getMessage(), ex);
        Response<Object> response = Response.internalServerError();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
