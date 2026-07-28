package com.sri.order_service.order.event;

import com.sri.order_service.integration.payment.enums.PaymentMethod;
import com.sri.order_service.integration.payment.enums.PaymentStatus;
import com.sri.order_service.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent implements Serializable {

    private Long orderId;

    private String orderNumber;

    private Long userId;

    private String email;

    private Long productId;

    private String productName;

    private Integer quantity;

    private BigDecimal totalAmount;

    private Long paymentId;

    private String transactionId;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private OrderStatus orderStatus;

    private LocalDateTime placedAt;
}
