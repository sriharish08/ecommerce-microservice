package com.sri.notification_service.listener;

import com.sri.notification_service.config.RabbitMQConfig;
import com.sri.notification_service.event.OrderPlacedEvent;
import com.sri.notification_service.message.EmailNotificationMessage;
import com.sri.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_PLACED_QUEUE)
    public void consume(OrderPlacedEvent event) {

        log.info("Received OrderPlacedEvent for orderNumber={}", event.getOrderNumber());

        EmailNotificationMessage message = EmailNotificationMessage.builder()
                .userId(event.getUserId())
                .email(event.getEmail())
                .subject("Order Confirmed - " + event.getOrderNumber())
                .message(buildMessage(event))
                .build();

        notificationService.processNotification(message);
    }

    private String buildMessage(OrderPlacedEvent event) {

        return """
                Hi,

                Your order has been placed successfully.

                Order Number: %s
                Product: %s (x%d)
                Total Amount: %s
                Payment Method: %s
                Transaction Id: %s

                Thank you for shopping with us.

                Regards,
                Support Team
                """.formatted(
                event.getOrderNumber(),
                event.getProductName(),
                event.getQuantity(),
                event.getTotalAmount(),
                event.getPaymentMethod(),
                event.getTransactionId()
        );
    }
}
