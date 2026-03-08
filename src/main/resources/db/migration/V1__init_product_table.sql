CREATE TABLE product (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(15) NOT NULL,
     price DECIMAL(19, 2) NOT NULL,
     image_url VARCHAR(255), -- 이 컬럼이 빠져있어서 에러가 났습니다!
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);