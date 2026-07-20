package com.sri.web_gateway.integration.order.client;

import com.sri.web_gateway.common.response.Response;
import com.sri.web_gateway.integration.order.dto.CreateOrderRequest;
import com.sri.web_gateway.integration.order.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "order-service",
        url = "${feign.client.order-service}"
)
public interface OrderFeignClient {

    @PostMapping
    Response<OrderResponse> createOrder(@RequestBody CreateOrderRequest request);

    @GetMapping("/{id}")
    Response<OrderResponse> getOrderById(@PathVariable("id") Long id);

    @GetMapping
    Response<List<OrderResponse>> getAllOrders();

    @DeleteMapping("/{id}")
    Response<Void> deleteOrder(@PathVariable("id") Long id);
}
