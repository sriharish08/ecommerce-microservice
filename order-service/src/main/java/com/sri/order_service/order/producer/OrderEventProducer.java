package com.sri.order_service.order.producer;

import com.sri.order_service.common.config.RabbitMQConfig;
import com.sri.order_service.order.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);

    private final RabbitTemplate rabbitTemplate;

    /**
     * Best-effort publish. The order is already saved and payment already succeeded by the
     * time this runs, so a broker outage must not fail the customer-facing request - it is
     * logged and swallowed instead. This leaves a window where an order is confirmed but its
     * notification is never sent; closing that gap in production would mean an outbox table
     * (or publisher-confirms + a retry/reconciliation job) rather than a direct publish.
     */
    public void publishOrderPlaced(OrderPlacedEvent event) {

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_PLACED_ROUTING_KEY,
                    event
            );

            log.info("Published OrderPlacedEvent for orderNumber={}", event.getOrderNumber());

        } catch (Exception ex) {
            log.error("Failed to publish OrderPlacedEvent for orderNumber={}. Reason: {}",
                    event.getOrderNumber(),
                    ex.getMessage(), ex);
        }
    }
}
