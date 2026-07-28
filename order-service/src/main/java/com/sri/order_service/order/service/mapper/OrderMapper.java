package com.sri.order_service.order.service.mapper;


import com.sri.order_service.order.dto.response.OrderResponse;
import com.sri.order_service.order.entity.Order;
import com.sri.order_service.order.event.OrderPlacedEvent;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order) {

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .productName(order.getProductName())
                .productPrice(order.getProductPrice())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .paymentId(order.getPaymentId())
                .transactionId(order.getTransactionId())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(order.getPaymentMethod())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    public static OrderPlacedEvent toPlacedEvent(Order order, String email) {

        return OrderPlacedEvent.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .email(email)
                .productId(order.getProductId())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .paymentId(order.getPaymentId())
                .transactionId(order.getTransactionId())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .orderStatus(order.getOrderStatus())
                .placedAt(order.getCreatedAt())
                .build();
    }
}
