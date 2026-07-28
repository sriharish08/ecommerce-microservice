package com.sri.order_service.order.service;


import com.sri.order_service.order.dto.request.CreateOrderRequest;
import com.sri.order_service.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request, Long userId, String email);

    OrderResponse getOrderById(Long id, Long requesterId, String requesterRole);

    List<OrderResponse> getAllOrders(Long requesterId, String requesterRole);

    void deleteOrder(Long id, Long requesterId, String requesterRole);

}