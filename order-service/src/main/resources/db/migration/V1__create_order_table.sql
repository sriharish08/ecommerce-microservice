CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    product_id BIGINT NOT NULL,

    product_name VARCHAR(255) NOT NULL,

    product_price DECIMAL(10,2) NOT NULL,

    quantity INT NOT NULL,

    total_amount DECIMAL(10,2) NOT NULL,

    order_status VARCHAR(30) NOT NULL,

    created_at DATETIME NOT NULL,

    updated_at DATETIME DEFAULT NULL
);