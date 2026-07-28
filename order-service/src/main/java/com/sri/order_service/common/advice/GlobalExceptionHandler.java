package com.sri.order_service.common.advice;

import com.sri.order_service.common.response.Response;
import com.sri.order_service.order.exception.CustomException;
import com.sri.order_service.order.exception.InsufficientStockException;
import com.sri.order_service.order.exception.OrderAccessDeniedException;
import com.sri.order_service.order.exception.OrderNotFoundException;
import com.sri.order_service.order.exception.PaymentFailedException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Response<Object>> handleOrderNotFound(OrderNotFoundException ex) {
        logger.error("OrderNotFoundException : {}", ex.getMessage());
        Response<Object> response = Response.notFound();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderAccessDeniedException.class)
    public ResponseEntity<Response<Object>> handleOrderAccessDenied(OrderAccessDeniedException ex) {
        logger.error("OrderAccessDeniedException : {}", ex.getMessage());
        Response<Object> response = Response.status(HttpStatus.FORBIDDEN);
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Response<Object>> handleInsufficientStock(InsufficientStockException ex) {
        logger.error("InsufficientStockException : {}", ex.getMessage());
        Response<Object> response = Response.conflict();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<Response<Object>> handlePaymentFailed(PaymentFailedException ex) {
        logger.error("PaymentFailedException : orderNumber={}, message={}", ex.getOrderNumber(), ex.getMessage());
        Response<Object> response = Response.status(HttpStatus.PAYMENT_REQUIRED);
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response<Object>> handleCustomException(CustomException ex) {
        logger.error("CustomException : {}", ex.getMessage());
        Response<Object> response = Response.serviceUnavailable();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
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

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Response<Object>> handleMissingHeader(MissingRequestHeaderException ex) {
        logger.error("MissingRequestHeaderException : {}", ex.getMessage());
        Response<Object> response = Response.badRequest();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Response<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        logger.error("MethodArgumentTypeMismatchException : {}", ex.getMessage());
        Response<Object> response = Response.badRequest();
        response.addErrorMsgToResponse(
                "Invalid value for parameter '" + ex.getName() + "'", ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
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
