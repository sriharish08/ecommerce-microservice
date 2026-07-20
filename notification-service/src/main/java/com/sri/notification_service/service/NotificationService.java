package com.sri.notification_service.service;

import com.sri.notification_service.message.EmailNotificationMessage;

public interface NotificationService {

    void processNotification(EmailNotificationMessage message);

}
