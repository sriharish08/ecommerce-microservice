package com.sri.auth_service.auth.producer;


import com.sri.auth_service.common.config.RabbitMQConfig;
import com.sri.auth_service.auth.entity.User;
import com.sri.auth_service.auth.enums.OtpPurpose;
import com.sri.auth_service.auth.message.EmailNotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOtp(User user, String otp, OtpPurpose purpose) {

        EmailNotificationMessage message = EmailNotificationMessage.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .subject(getSubject(purpose))
                .message(buildMessage(user.getUsername(), otp, purpose))
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                message
        );
    }

    private String getSubject(OtpPurpose purpose) {

        return switch (purpose) {
            case REGISTRATION -> "Verify Your Account";
            case LOGIN -> "Login Verification OTP";
            case PASSWORD_RESET -> "Password Reset OTP";
        };
    }

    private String buildMessage(String username,
                                String otp,
                                OtpPurpose purpose) {

        return switch (purpose) {

            case REGISTRATION ->
                    """
                    Hi %s,

                    Welcome!

                    Your registration OTP is:

                    %s

                    This OTP will expire shortly.

                    Regards,
                    Support Team
                    """.formatted(username, otp);

            case LOGIN ->
                    """
                    Hi %s,

                    Your login OTP is:

                    %s

                    If you did not request this login,
                    please ignore this email.

                    Regards,
                    Support Team
                    """.formatted(username, otp);

            case PASSWORD_RESET ->
                    """
                    Hi %s,

                    Your password reset OTP is:

                    %s

                    If you didn't request this,
                    please ignore this email.

                    Regards,
                    Support Team
                    """.formatted(username, otp);
        };
    }
}
