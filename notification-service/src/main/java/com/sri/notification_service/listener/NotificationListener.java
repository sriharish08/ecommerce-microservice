package com.sri.notification_service.listener;

import com.sri.notification_service.config.RabbitMQConfig;
import com.sri.notification_service.message.EmailNotificationMessage;
import com.sri.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consume(EmailNotificationMessage message) {

        notificationService.processNotification(message);

    }
}