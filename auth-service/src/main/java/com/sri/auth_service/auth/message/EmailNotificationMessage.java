package com.sri.auth_service.auth.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationMessage {

    private Long userId;

    private String email;

    private String subject;

    private String message;
}