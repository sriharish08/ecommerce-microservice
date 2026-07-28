package com.sri.order_service.order.controller;

import com.sri.order_service.common.response.Response;
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
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Email") String email) {

        Response<OrderResponse> response = Response.created();
        response.setPayload(orderService.createOrder(request, userId, email));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderResponse>> getOrderById(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        Response<OrderResponse> response = Response.ok();
        response.setPayload(orderService.getOrderById(id, userId, role));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response<List<OrderResponse>>> getAllOrders(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        Response<List<OrderResponse>> response = Response.ok();
        response.setPayload(orderService.getAllOrders(userId, role));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteOrder(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        orderService.deleteOrder(id, userId, role);
        Response<Void> response = Response.ok();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
