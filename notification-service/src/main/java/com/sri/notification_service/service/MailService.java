package com.sri.notification_service.service;

import com.sri.notification_service.event.DailyOrderReportEvent;

public interface MailService {

    void sendEmail(String to,
                   String subject,
                   String message);

    void publishDailyOrderReport(DailyOrderReportEvent event);

}