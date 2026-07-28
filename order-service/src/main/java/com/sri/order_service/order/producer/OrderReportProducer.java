package com.sri.order_service.order.producer;


import com.sri.order_service.common.config.RabbitMQConfig;
import com.sri.order_service.order.event.DailyOrderReportEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class OrderReportProducer {
    private static final Logger log = LoggerFactory.getLogger(OrderReportProducer.class);

    private final RabbitTemplate rabbitTemplate;

    /**
     * Publishes the daily order report event to RabbitMQ.
     * This is a best-effort publish. If RabbitMQ is unavailable,
     * the scheduler should continue running and the failure is logged.
     */
    public void publishDailyOrderReport(DailyOrderReportEvent event) {

        try {

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.DAILY_ORDER_REPORT_ROUTING_KEY,
                    event
            );

            log.info(
                    "Published DailyOrderReportEvent for reportDate={}, totalOrders={}, totalRevenue={}",
                    event.getReportDate(),
                    event.getTotalOrders(),
                    event.getTotalRevenue()
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to publish DailyOrderReportEvent for reportDate={}. Reason: {}",
                    event.getReportDate(),
                    ex.getMessage(),
                    ex
            );
        }
    }
}