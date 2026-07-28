package com.sri.order_service.order.event;

import com.sri.order_service.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummary {

    private Long orderId;

    private String productName;

    private Integer quantity;

    private BigDecimal amount;

    private OrderStatus status;
}
