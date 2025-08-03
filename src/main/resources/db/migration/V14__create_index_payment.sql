CREATE INDEX idx_payment_order_id_order_item_id ON payment(order_id, order_item_id);
CREATE INDEX idx_payment_payment_status_created_at ON payment(payment_status, created_at);
CREATE INDEX idx_payment_created_at ON payment(created_at);
