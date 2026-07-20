package com.sri.notification_service.service;

public interface MailService {

    void sendEmail(String to,
                   String subject,
                   String message);

}