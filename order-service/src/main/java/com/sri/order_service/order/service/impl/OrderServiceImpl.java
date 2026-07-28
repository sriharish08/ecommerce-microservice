package com.sri.order_service.order.service.impl;

import com.sri.order_service.integration.payment.client.PaymentClient;
import com.sri.order_service.integration.payment.dto.PaymentRequest;
import com.sri.order_service.integration.payment.dto.PaymentResponse;
import com.sri.order_service.integration.payment.enums.PaymentStatus;
import com.sri.order_service.integration.product.client.ProductClient;
import com.sri.order_service.integration.product.dto.ProductResponse;
import com.sri.order_service.order.dto.request.CreateOrderRequest;
import com.sri.order_service.order.dto.response.OrderResponse;
import com.sri.order_service.order.entity.Order;
import com.sri.order_service.order.enums.OrderStatus;
import com.sri.order_service.order.event.OrderPlacedEvent;
import com.sri.order_service.order.exception.InsufficientStockException;
import com.sri.order_service.order.exception.OrderAccessDeniedException;
import com.sri.order_service.order.exception.OrderNotFoundException;
import com.sri.order_service.order.exception.PaymentFailedException;
import com.sri.order_service.order.producer.OrderEventProducer;
import com.sri.order_service.order.repository.OrderRepository;
import com.sri.order_service.order.service.OrderService;
import com.sri.order_service.order.service.mapper.OrderMapper;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;
    private final OrderEventProducer orderEventProducer;

    private static final String ADMIN_ROLE = "ADMIN";

    /**
     * Orchestrates order creation: Product -> stock check -> reserve stock -> persist
     * (PENDING) -> Payment -> update -> (on failure) restore stock -> publish.
     * Stock is reserved (decremented) before payment runs, not after, so two concurrent
     * checkouts for the last unit can't both pass validation and only one payment is ever
     * attempted against inventory that's actually available; a failed payment restores it.
     * Not wrapped in @Transactional - it makes blocking outbound Feign calls, and holding a
     * DB transaction/connection open across network round-trips would tie up pool resources
     * for no benefit; each of the two order writes below commits independently.
     */
    @Override
    public OrderResponse createOrder(CreateOrderRequest request, Long userId, String email) {

        ProductResponse product = productClient.getProductById(request.getProductId());

        if (request.getQuantity() > product.getQuantity()) {
            throw new InsufficientStockException(
                    "Insufficient stock for productId=" + product.getId());
        }

        productClient.decrementStock(product.getId(), request.getQuantity());

        BigDecimal totalAmount = product.getPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Order is persisted first, PENDING, so it has a real id to correlate the payment
        // with - payment-service requires a non-null orderId and uses it for idempotency.
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .userId(userId)
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .quantity(request.getQuantity())
                .totalAmount(totalAmount)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .orderStatus(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        log.info("Processing payment for orderNumber={}, orderId={}, userId={}, amount={}",
                orderNumber, savedOrder.getId(), userId, totalAmount);

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(savedOrder.getId())
                .userId(userId)
                .amount(totalAmount)
                .paymentMethod(request.getPaymentMethod())
                .build();

        PaymentResponse paymentResponse = paymentClient.processPayment(paymentRequest).getPayload();
        PaymentStatus paymentStatus = paymentResponse.getPaymentStatus();

        // SUCCESS confirms the order outright; PENDING (e.g. Cash on Delivery, nothing
        // collected yet) still confirms the order itself - payment settles later on delivery.
        boolean orderConfirmed = paymentStatus == PaymentStatus.SUCCESS || paymentStatus == PaymentStatus.PENDING;

        savedOrder.setPaymentId(paymentResponse.getPaymentId());
        savedOrder.setTransactionId(paymentResponse.getTransactionId());
        savedOrder.setPaymentStatus(paymentStatus);
        savedOrder.setOrderStatus(orderConfirmed ? OrderStatus.CONFIRMED : OrderStatus.FAILED);

        // Persisted either way for audit/reconciliation (e.g. so a customer or support agent can
        // see why a checkout attempt failed) rather than only ever recording successful orders.
        Order finalOrder = orderRepository.save(savedOrder);

        if (!orderConfirmed) {

            log.warn("Payment failed for orderNumber={}, paymentStatus={}", orderNumber, paymentStatus);

            productClient.restoreStock(product.getId(), request.getQuantity());

            throw new PaymentFailedException(orderNumber,
                    "Payment " + paymentStatus + " for order " + orderNumber);
        }

        // Only a SUCCESS payment settles money now, so only that case gets a confirmation
        // email; PENDING (e.g. Cash on Delivery) confirms the order but nothing has been
        // charged yet, so an email here would be misleading.
        if (paymentStatus == PaymentStatus.SUCCESS) {
            OrderPlacedEvent event = OrderMapper.toPlacedEvent(finalOrder, email);
            orderEventProducer.publishOrderPlaced(event);
        }

        log.info("Order created successfully: orderNumber={}, orderId={}", orderNumber, finalOrder.getId());

        return OrderMapper.toResponse(finalOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id, Long requesterId, String requesterRole) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        assertOwnerOrAdmin(order, requesterId, requesterRole);

        return OrderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders(Long requesterId, String requesterRole) {

        List<Order> orders = ADMIN_ROLE.equals(requesterRole)
                ? orderRepository.findAll()
                : orderRepository.findByUserId(requesterId);

        return orders.stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteOrder(Long id, Long requesterId, String requesterRole) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        assertOwnerOrAdmin(order, requesterId, requesterRole);

        orderRepository.delete(order);
    }

    private void assertOwnerOrAdmin(Order order, Long requesterId, String requesterRole) {

        if (ADMIN_ROLE.equals(requesterRole)) {
            return;
        }

        if (!order.getUserId().equals(requesterId)) {
            throw new OrderAccessDeniedException("You do not have access to this order");
        }
    }

}
