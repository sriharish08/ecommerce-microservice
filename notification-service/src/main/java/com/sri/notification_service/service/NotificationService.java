package com.sri.notification_service.service;

import com.sri.notification_service.dto.response.NotificationHistoryResponse;
import com.sri.notification_service.message.EmailNotificationMessage;

import java.util.List;

public interface NotificationService {

    void processNotification(EmailNotificationMessage message);

    NotificationHistoryResponse getById(Long id, Long requesterId, String requesterRole);

    List<NotificationHistoryResponse> getByUserId(Long userId, Long requesterId, String requesterRole);

}
