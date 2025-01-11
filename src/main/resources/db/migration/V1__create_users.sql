CREATE TABLE users (
                       id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                       user_name VARCHAR(50) NOT NULL,
                       password VARCHAR(20) NOT NULL,
                       phone_number VARCHAR(20) NULL,
                       email VARCHAR(30) NULL
)
