package com.sri.web_gateway.integration.order.client;

import com.sri.web_gateway.exception.ServiceUnavailableException;
import com.sri.web_gateway.integration.order.dto.CreateOrderRequest;
import com.sri.web_gateway.integration.order.dto.OrderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderClient {

    private static final Logger log = LoggerFactory.getLogger(OrderClient.class);

    private final OrderFeignClient orderFeignClient;

    @CircuitBreaker(name = "orderCreate", fallbackMethod = "createOrderFallback")
    public OrderResponse createOrder(CreateOrderRequest request) {

        log.info("Calling Order Service to create order for productId={}", request.getProductId());

        return orderFeignClient.createOrder(request).getPayload();
    }

    private OrderResponse createOrderFallback(CreateOrderRequest request, Throwable exception) {
        return fail("order-service", exception);
    }

    @Retry(name = "orderGetById")
    @CircuitBreaker(name = "orderGetById", fallbackMethod = "getOrderByIdFallback")
    public OrderResponse getOrderById(Long id) {

        log.info("Calling Order Service for orderId={}", id);

        return orderFeignClient.getOrderById(id).getPayload();
    }

    private OrderResponse getOrderByIdFallback(Long id, Throwable exception) {
        return fail("order-service", exception);
    }

    @Retry(name = "orderGetAll")
    @CircuitBreaker(name = "orderGetAll", fallbackMethod = "getAllOrdersFallback")
    public List<OrderResponse> getAllOrders() {

        log.info("Calling Order Service to fetch all orders");

        return orderFeignClient.getAllOrders().getPayload();
    }

    private List<OrderResponse> getAllOrdersFallback(Throwable exception) {
        return fail("order-service", exception);
    }

    @Retry(name = "orderDelete")
    @CircuitBreaker(name = "orderDelete", fallbackMethod = "deleteOrderFallback")
    public void deleteOrder(Long id) {

        log.info("Calling Order Service to delete orderId={}", id);

        orderFeignClient.deleteOrder(id);
    }

    private void deleteOrderFallback(Long id, Throwable exception) {
        fail("order-service", exception);
    }

    private <T> T fail(String serviceId, Throwable exception) {

        log.error("Order Service call failed. Reason: {}", exception.getMessage());

        throw new ServiceUnavailableException(serviceId, serviceId + " is temporarily unavailable, please try again later");
    }
}
