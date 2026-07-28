package com.sri.order_service.order.exception;

import lombok.Getter;

@Getter
public class PaymentFailedException extends RuntimeException {

    private final String orderNumber;

    public PaymentFailedException(String orderNumber, String message) {
        super(message);
        this.orderNumber = orderNumber;
    }
}
