CREATE TABLE users
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    username VARCHAR(100) NOT NULL UNIQUE,

    email VARCHAR(150) NOT NULL UNIQUE,

    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(20) NOT NULL,

    enabled BOOLEAN NOT NULL DEFAULT TRUE,

    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,

    account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,

    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME NOT NULL,

    updated_at DATETIME NOT NULL
);