package com.sri.payment_service.exception;

public class PaymentAccessDeniedException extends RuntimeException {

    public PaymentAccessDeniedException(String message) {
        super(message);
    }
}
