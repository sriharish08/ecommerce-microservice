package com.sri.web_gateway.common.advice;

import com.sri.web_gateway.common.response.Response;
import com.sri.web_gateway.common.response.ResponseBuilder;
import com.sri.web_gateway.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Response<Object>> handleServiceUnavailable(ServiceUnavailableException ex) {

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("service", ex.getServiceId());

        return ResponseBuilder.error(ex.getMessage(), metadata, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseBuilder.error(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleException(Exception ex) {
        return ResponseBuilder.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
