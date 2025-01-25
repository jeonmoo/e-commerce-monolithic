ALTER TABLE order_item
CHANGE price final_price DECIMAL(20, 6) NOT NULL,
ADD COLUMN discount_type VARCHAR(20) NULL,
ADD COLUMN discount_value DECIMAL(20, 6) NULL,
ADD COLUMN origin_price DECIMAL(20, 6) NOT NULL
;
