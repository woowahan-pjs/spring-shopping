CREATE TABLE member (
    id BINARY(16) PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE wish (
    id BINARY(16) PRIMARY KEY,
    member_id BINARY(16) NOT NULL,
    product_id BINARY(16) NOT NULL,
    wished_price BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT fk_wish_member FOREIGN KEY (member_id) REFERENCES member(id),
    INDEX idx_wish_product_id (product_id),
    UNIQUE INDEX uk_wish_member_product (member_id, product_id)
);
