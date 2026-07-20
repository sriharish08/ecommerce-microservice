package com.sri.order_service.order.controller;

import com.sri.order_service.common.response.Response;
import com.sri.order_service.common.response.ResponseBuilder;
import com.sri.order_service.order.dto.request.CreateOrderRequest;
import com.sri.order_service.order.dto.response.OrderResponse;
import com.sri.order_service.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Response<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        OrderResponse response = orderService.createOrder(request);

        return ResponseBuilder.success(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderResponse>> getOrderById(
            @PathVariable("id") Long id) {

        OrderResponse response = orderService.getOrderById(id);

        return ResponseBuilder.success(response);
    }

    @GetMapping
    public ResponseEntity<Response<List<OrderResponse>>> getAllOrders() {

        List<OrderResponse> response = orderService.getAllOrders();

        return ResponseBuilder.success(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteOrder(
            @PathVariable("id") Long id) {

        orderService.deleteOrder(id);

        return ResponseBuilder.success(null);
    }
}
