package com.sri.web_gateway.integration.notification.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationHistoryResponse {

    private Long id;

    private Long userId;

    private String email;

    private String subject;

    private String status;

    private String notificationType;

    private String failureReason;

    private LocalDateTime createdAt;
}
