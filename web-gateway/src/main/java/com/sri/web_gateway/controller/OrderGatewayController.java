package com.sri.web_gateway.controller;

import com.sri.web_gateway.common.response.Response;
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
        Response<OrderResponse> response = Response.created();
        response.setPayload(orderClient.createOrder(request));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderResponse>> getOrderById(@PathVariable("id") Long id) {
        Response<OrderResponse> response = Response.ok();
        response.setPayload(orderClient.getOrderById(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response<List<OrderResponse>>> getAllOrders() {
        Response<List<OrderResponse>> response = Response.ok();
        response.setPayload(orderClient.getAllOrders());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteOrder(@PathVariable("id") Long id) {
        orderClient.deleteOrder(id);
        Response<Void> response = Response.ok();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
