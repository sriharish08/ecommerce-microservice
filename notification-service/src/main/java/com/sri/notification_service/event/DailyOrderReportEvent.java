package com.sri.notification_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyOrderReportEvent {

    private LocalDate reportDate;

    private Integer totalOrders;

    private BigDecimal totalRevenue;

    private List<OrderSummary> orders;
}

