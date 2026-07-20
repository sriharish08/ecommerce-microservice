CREATE TABLE notification_history
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    user_id BIGINT NOT NULL,

    email VARCHAR(255) NOT NULL,

    subject VARCHAR(255) NOT NULL,

    message TEXT NOT NULL,

    status VARCHAR(20) NOT NULL,

    notification_type VARCHAR(50) NOT NULL,

    failure_reason TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);