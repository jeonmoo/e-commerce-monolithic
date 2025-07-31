CREATE INDEX idx_product_category_id_created_at ON product(category_id, created_at);
CREATE INDEX idx_product_product_name_created_at ON product(product_name, created_at);
CREATE INDEX idx_product_final_price_created_at ON product(final_price, created_at);
CREATE INDEX idx_product_created_at ON product(created_at);
