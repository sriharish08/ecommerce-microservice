package com.sri.notification_service.event;


import com.sri.notification_service.event.enums.OrderStatus;
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
