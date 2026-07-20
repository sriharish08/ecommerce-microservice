package com.sri.web_gateway.controller;

import com.sri.web_gateway.common.response.Response;
import com.sri.web_gateway.common.response.ResponseBuilder;
import com.sri.web_gateway.integration.order.client.OrderClient;
import com.sri.web_gateway.integration.order.dto.CreateOrderRequest;
import com.sri.web_gateway.integration.order.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderGatewayController {

    private final OrderClient orderClient;

    @PostMapping
    public ResponseEntity<Response<OrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseBuilder.success(orderClient.createOrder(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderResponse>> getOrderById(@PathVariable("id") Long id) {
        return ResponseBuilder.success(orderClient.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<Response<List<OrderResponse>>> getAllOrders() {
        return ResponseBuilder.success(orderClient.getAllOrders());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteOrder(@PathVariable("id") Long id) {
        orderClient.deleteOrder(id);
        return ResponseBuilder.success(null);
    }
}
