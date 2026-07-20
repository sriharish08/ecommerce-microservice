package com.sri.order_service.order.service.mapper;


import com.sri.order_service.order.dto.response.OrderResponse;
import com.sri.order_service.order.entity.Order;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .productName(order.getProductName())
                .productPrice(order.getProductPrice())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}