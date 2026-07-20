package com.sri.order_service.order.exception;


public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long id) {
        super("Order not found with id : " + id);
    }
}