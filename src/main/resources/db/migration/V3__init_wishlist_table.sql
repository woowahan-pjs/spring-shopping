CREATE TABLE wishlist (
  id BigInt AUTO_INCREMENT PRIMARY KEY,
  member_id BigInt NOT NULL,
  product_id BigInt NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_wishlist_member FOREIGN KEY (member_id) REFERENCES member(id),
  CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) REFERENCES product(id),

  UNIQUE KEY uk_member_product (member_id, product_id)
);