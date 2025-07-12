CREATE INDEX idx_orders_user_id_created_at ON orders(user_id, created_at);
CREATE INDEX idx_orders_order_status_created_at ON orders(order_status, created_at);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_total_final_price_created_at ON orders(total_final_price, created_at);
