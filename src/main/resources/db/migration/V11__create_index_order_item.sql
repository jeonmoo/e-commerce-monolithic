CREATE INDEX idx_order_item_order_id ON order_item(order_id);
CREATE INDEX idx_order_item_product_id_created_at ON order_item(product_id, created_at);
CREATE INDEX idx_order_item_origin_price ON order_item(origin_price);
