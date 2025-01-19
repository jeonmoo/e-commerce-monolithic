CREATE TABLE payment (
  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  pay_amount DECIMAL(20, 6) NOT NULL,
  payment_status VARCHAR(20) NOT NULL,
  reason TEXT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NULL
)
