package com.sri.order_service.order.service.impl;

import com.sri.order_service.integration.product.client.ProductClient;
import com.sri.order_service.integration.product.client.ProductFeignClient;
import com.sri.order_service.integration.product.dto.ProductResponse;
import com.sri.order_service.order.dto.request.CreateOrderRequest;
import com.sri.order_service.order.dto.response.OrderResponse;
import com.sri.order_service.order.entity.Order;
import com.sri.order_service.order.enums.OrderStatus;
import com.sri.order_service.order.exception.OrderNotFoundException;
import com.sri.order_service.order.service.OrderService;
import com.sri.order_service.order.service.mapper.OrderMapper;
import com.sri.order_service.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {

        ProductResponse product = productClient.getProductById(request.getProductId());
        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = Order.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .quantity(request.getQuantity())
                .totalAmount(totalAmount)
                .orderStatus(OrderStatus.CREATED)
                .build();

        Order savedOrder = orderRepository.save(order);

        return OrderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return OrderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteOrder(Long id) {

        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }

        orderRepository.deleteById(id);
    }
}
