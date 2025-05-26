CREATE TABLE discount (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    discount_name VARCHAR(50) NOT NULL,
    discount_type VARCHAR(30) NOT NULL,
    discount_amount DECIMAL(20, 6) NOT NULL,
    discount_percent DECIMAL(20, 6) NOT NULL,
    start_date_time DATETIME NULL,
    end_date_time DATETIME NULL,
    description TEXT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL
)
