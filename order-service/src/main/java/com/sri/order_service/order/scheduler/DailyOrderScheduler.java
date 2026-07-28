package com.sri.order_service.order.scheduler;

import com.sri.order_service.order.entity.Order;
import com.sri.order_service.order.event.DailyOrderReportEvent;
import com.sri.order_service.order.event.OrderSummary;
import com.sri.order_service.order.producer.OrderReportProducer;
import com.sri.order_service.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyOrderScheduler {

    private final OrderRepository orderRepository;
    private final OrderReportProducer producer;

    /* Every day morning 9'o Clk Yesterday Report will be sent to the owner of the shop **/
    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Kolkata")
    public void sendDailyReport() {

        LocalDate yesterday = LocalDate.now().minusDays(1);

        LocalDateTime start = yesterday.atStartOfDay();
        LocalDateTime end = yesterday.plusDays(1).atStartOfDay();

        List<Order> orders =
                orderRepository.findByCreatedAtBetween(start, end);

        List<OrderSummary> summaries = orders.stream()
                .map(this::mapToSummary)
                .toList();

        BigDecimal revenue = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DailyOrderReportEvent event =
                DailyOrderReportEvent.builder()
                        .reportDate(yesterday)
                        .totalOrders(orders.size())
                        .totalRevenue(revenue)
                        .orders(summaries)
                        .build();

        producer.publishDailyOrderReport(event);

        log.info("Daily Order Report Published Successfully");
    }

    private OrderSummary mapToSummary(Order order) {

        return OrderSummary.builder()
                .orderId(order.getId())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .amount(order.getTotalAmount())
                .status(order.getOrderStatus())
                .build();
    }
}
