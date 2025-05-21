CREATE TABLE product (
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	category_id BIGINT NOT NULL,
	product_name TEXT NOT NULL,
	quantity INT NOT NULL,
	final_price DECIMAL(20, 6) NOT NULL,
	origin_price DECIMAL(20, 6) NOT NULL,
	discount_price DECIMAL(20, 6) NOT NULL,
	is_delete TINYINT NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NULL
)
