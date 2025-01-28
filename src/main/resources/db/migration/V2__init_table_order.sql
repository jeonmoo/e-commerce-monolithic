CREATE TABLE orders (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    order_status VARCHAR(30) NOT NULL,
    address TEXT NOT NULL,
    total_final_price DECIMAL(20, 6) NOT NULL,
    total_origin_price DECIMAL(20, 6) NOT NULL,
    total_discount_price DECIMAL(20, 6) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL
)
