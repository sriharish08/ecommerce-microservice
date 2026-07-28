package com.sri.order_service.order.dto.response;


import com.sri.order_service.integration.payment.enums.PaymentMethod;
import com.sri.order_service.integration.payment.enums.PaymentStatus;
import com.sri.order_service.order.enums.OrderStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;

    private String orderNumber;

    private Long userId;

    private Long productId;

    private String productName;

    private BigDecimal productPrice;

    private Integer quantity;

    private BigDecimal totalAmount;

    private Long paymentId;

    private String transactionId;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private OrderStatus orderStatus;

    private LocalDateTime createdAt;
}
