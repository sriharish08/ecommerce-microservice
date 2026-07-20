package com.sri.notification_service.service.impl;

import com.sri.notification_service.entity.NotificationHistory;
import com.sri.notification_service.enums.NotificationStatus;
import com.sri.notification_service.enums.NotificationType;
import com.sri.notification_service.message.EmailNotificationMessage;
import com.sri.notification_service.repository.NotificationRepository;
import com.sri.notification_service.service.MailService;
import com.sri.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    final static Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final MailService mailService;
    private final NotificationRepository repository;

    @Override
    public void processNotification(EmailNotificationMessage message) {

        NotificationHistory history = NotificationHistory.builder()
                .userId(message.getUserId())
                .email(message.getEmail())
                .subject(message.getSubject())
                .message(message.getMessage())
                .createdAt(LocalDateTime.now())
                .status(NotificationStatus.PENDING)
                .notificationType(NotificationType.EMAIL)
                .build();

        try {

            mailService.sendEmail(
                    message.getEmail(),
                    message.getSubject(),
                    message.getMessage());

            history.setStatus(NotificationStatus.SENT);

            log.info("Email sent successfully to {}", message.getEmail());

        } catch (MailException ex) {

            history.setStatus(NotificationStatus.FAILED);
            history.setFailureReason(ex.getMessage());

            log.error("Failed to send email to {}", message.getEmail(), ex);
        }

        repository.save(history);

    }
}
