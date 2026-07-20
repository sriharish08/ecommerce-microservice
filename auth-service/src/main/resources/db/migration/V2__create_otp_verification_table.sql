CREATE TABLE otp_verification
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    user_id BIGINT NOT NULL,

    otp VARCHAR(6) NOT NULL,

    purpose VARCHAR(30) NOT NULL,

    expires_at DATETIME NOT NULL,

    created_at DATETIME NOT NULL,

    CONSTRAINT fk_otp_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_user_purpose
        UNIQUE (user_id, purpose)
);

CREATE INDEX idx_otp_expiry
    ON otp_verification (expires_at);