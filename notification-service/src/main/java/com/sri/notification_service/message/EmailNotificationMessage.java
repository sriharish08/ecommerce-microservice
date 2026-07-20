package com.sri.notification_service.message;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailNotificationMessage implements Serializable {

    private Long userId;

    private String email;

    private String subject;

    private String message;

}