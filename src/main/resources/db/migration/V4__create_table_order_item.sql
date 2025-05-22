CREATE TABLE order_item (
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	order_id BIGINT NOT NULL,
	product_id BIGINT NOT NULL,
    order_status VARCHAR(30) NOT NULL,
	quantity INT NOT NULL,
    origin_price DECIMAL(20, 6) NOT NULL,
    final_price DECIMAL(20, 6) NOT NULL,
    discount_price DECIMAL(20, 6) NOT NULL,
    created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NULL
)
