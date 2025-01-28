CREATE TABLE order_item_discount (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_item_id BIGINT NOT NULL,
    discount_id BIGINT NOT NULL
)
