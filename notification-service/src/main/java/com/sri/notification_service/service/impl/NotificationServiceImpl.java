package com.sri.notification_service.service.impl;

import com.sri.notification_service.dto.response.NotificationHistoryResponse;
import com.sri.notification_service.entity.NotificationHistory;
import com.sri.notification_service.enums.NotificationStatus;
import com.sri.notification_service.enums.NotificationType;
import com.sri.notification_service.exception.NotificationAccessDeniedException;
import com.sri.notification_service.exception.NotificationNotFoundException;
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
import java.util.List;


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

    private static final String ADMIN_ROLE = "ADMIN";

    @Override
    public NotificationHistoryResponse getById(Long id, Long requesterId, String requesterRole) {

        NotificationHistory history = repository.findById(id)
                .orElseThrow(() ->
                        new NotificationNotFoundException("Notification not found for id=" + id));

        if (!ADMIN_ROLE.equals(requesterRole) && !history.getUserId().equals(requesterId)) {
            throw new NotificationAccessDeniedException("You do not have access to this notification");
        }

        return toResponse(history);
    }

    @Override
    public List<NotificationHistoryResponse> getByUserId(Long userId, Long requesterId, String requesterRole) {

        if (!ADMIN_ROLE.equals(requesterRole) && !userId.equals(requesterId)) {
            throw new NotificationAccessDeniedException("You do not have access to this user's notifications");
        }

        return repository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private NotificationHistoryResponse toResponse(NotificationHistory history) {

        NotificationHistoryResponse response = new NotificationHistoryResponse();

        response.setId(history.getId());
        response.setUserId(history.getUserId());
        response.setEmail(history.getEmail());
        response.setSubject(history.getSubject());
        response.setStatus(history.getStatus());
        response.setNotificationType(history.getNotificationType());
        response.setFailureReason(history.getFailureReason());
        response.setCreatedAt(history.getCreatedAt());

        return response;
    }
}
