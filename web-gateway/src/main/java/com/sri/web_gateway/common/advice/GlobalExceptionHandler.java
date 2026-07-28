package com.sri.web_gateway.common.advice;

import com.sri.web_gateway.common.response.Response;
import com.sri.web_gateway.exception.ServiceUnavailableException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Response<Object>> handleServiceUnavailable(ServiceUnavailableException ex) {
        logger.error("ServiceUnavailableException : {}", ex.getMessage());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("service", ex.getServiceId());

        Response<Object> response = Response.serviceUnavailable();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        response.setMetadata(metadata);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Response<Object>> handleFeignException(FeignException ex) {

        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }

        logger.error("FeignException : status={}, body={}", ex.status(), ex.contentUTF8());
        Response<Object> response = Response.status(status);
        response.addErrorMsgToResponse("Downstream service rejected the request", ex);
        response.setErrors(ex.contentUTF8());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Response<Object>> handleNoResourceFound(NoResourceFoundException ex) {
        Response<Object> response = Response.notFound();
        response.addErrorMsgToResponse("No such endpoint", ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

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
