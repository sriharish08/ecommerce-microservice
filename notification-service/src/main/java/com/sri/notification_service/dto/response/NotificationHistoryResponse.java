package com.sri.notification_service.dto.response;

import com.sri.notification_service.enums.NotificationStatus;
import com.sri.notification_service.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationHistoryResponse {

    private Long id;

    private Long userId;

    private String email;

    private String subject;

    private NotificationStatus status;

    private NotificationType notificationType;

    private String failureReason;

    private LocalDateTime createdAt;
}
