CREATE TABLE category (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT NULL,
    depth INT NOT NULL,
    sort INT NOT NULL,
    category_name VARCHAR(30) NOT NULL,
    is_delete TINYINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL
)
