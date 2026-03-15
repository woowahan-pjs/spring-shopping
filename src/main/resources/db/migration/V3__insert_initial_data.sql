-- 초기 ADMIN 계정
INSERT INTO member (email, password, role, created_at, updated_at)
VALUES ('admin@shop.com', '$2a$10$ihtT4kJSKDZccm9HepH4S.r6dvMPnrN//gFQRHRTySjlFpczQoYTi', 'ADMIN', NOW(), NOW());

-- 샘플 상품
INSERT INTO product (name, price, image_url, created_at, updated_at) VALUES ('치킨', 20000, 'https://c.com/chiken.jpg', NOW(), NOW());
INSERT INTO product (name, price, image_url, created_at, updated_at) VALUES ('피자', 20000, 'https://p.com/pizza.jpg', NOW(), NOW());
INSERT INTO product (name, price, image_url, created_at, updated_at) VALUES ('스파게티', 20000, 'https://s.com/spaghetti.jpg', NOW(), NOW());
