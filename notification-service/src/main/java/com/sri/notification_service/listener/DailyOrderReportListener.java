package com.sri.notification_service.listener;


import com.sri.notification_service.config.RabbitMQConfig;

import com.sri.notification_service.event.DailyOrderReportEvent;
import com.sri.notification_service.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyOrderReportListener {

    private final MailService mailService;

    @RabbitListener(queues = RabbitMQConfig.DAILY_ORDER_REPORT_QUEUE)
    public void consumeDailyOrderReport(DailyOrderReportEvent event) {

        log.info("Received DailyOrderReportEvent for reportDate={}", event.getReportDate());

        try {

            mailService.publishDailyOrderReport(event);

            log.info("Daily Order Report email sent successfully.");

        } catch (Exception ex) {

            log.error("Failed to send Daily Order Report email. Reason: {}", ex.getMessage(), ex);
        }
    }
}