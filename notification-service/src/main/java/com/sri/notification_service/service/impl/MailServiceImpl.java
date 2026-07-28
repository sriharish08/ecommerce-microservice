package com.sri.notification_service.service.impl;

import com.sri.notification_service.event.DailyOrderReportEvent;
import com.sri.notification_service.event.OrderSummary;
import com.sri.notification_service.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to,
                          String subject,
                          String message) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(message);

        mailSender.send(mail);
    }

    @Override
    public void publishDailyOrderReport(DailyOrderReportEvent event) {

        String ownerEmail = "sriharish552@gmail.com";

        StringBuilder body = new StringBuilder();

        body.append("Daily Order Report\n\n");
        body.append("Report Date : ").append(event.getReportDate()).append("\n");
        body.append("Total Orders : ").append(event.getTotalOrders()).append("\n");
        body.append("Total Revenue : ₹").append(event.getTotalRevenue()).append("\n\n");

        body.append("---------------------------------------\n");

        for (OrderSummary order : event.getOrders()) {

            body.append("Order ID      : ").append(order.getOrderId()).append("\n");
            body.append("Product       : ").append(order.getProductName()).append("\n");
            body.append("Quantity      : ").append(order.getQuantity()).append("\n");
            body.append("Amount        : ₹").append(order.getAmount()).append("\n");
            body.append("Order Status  : ").append(order.getStatus()).append("\n");
            body.append("---------------------------------------\n");
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(ownerEmail);
        message.setSubject("Daily Order Report - " + event.getReportDate());
        message.setText(body.toString());

        mailSender.send(message);

        log.info("Daily Order Report email sent to {}", ownerEmail);
    }
}