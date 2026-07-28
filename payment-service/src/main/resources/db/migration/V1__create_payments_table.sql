CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    order_id BIGINT NOT NULL,

    user_id BIGINT NOT NULL,

    amount DECIMAL(10,2) NOT NULL,

    payment_method VARCHAR(30) NOT NULL,

    payment_status VARCHAR(30) NOT NULL,

    transaction_id VARCHAR(255) NOT NULL UNIQUE,

    created_at DATETIME NOT NULL,

    updated_at DATETIME NOT NULL
);