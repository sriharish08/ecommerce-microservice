package com.sri.order_service.order.service;


import com.sri.order_service.order.dto.request.CreateOrderRequest;
import com.sri.order_service.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrders();

    void deleteOrder(Long id);
}