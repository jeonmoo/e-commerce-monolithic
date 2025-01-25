ALTER TABLE orders
CHANGE total_price total_final_price DECIMAL(20, 6) NOT NULL,
ADD COLUMN total_origin_price DECIMAL(20, 6) NOT NULL
;
