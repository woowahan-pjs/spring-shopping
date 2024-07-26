-- admin definition
USE shopping;

CREATE TABLE `admins` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `email` varchar(255) DEFAULT NULL,
                                  `name` varchar(255) DEFAULT NULL,
                                  `password` varchar(255) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- customers definition

CREATE TABLE `customers` (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `address` varchar(255) DEFAULT NULL,
                                      `birth` varchar(255) DEFAULT NULL,
                                      `email` varchar(255) DEFAULT NULL,
                                      `name` varchar(255) DEFAULT NULL,
                                      `password` varchar(255) DEFAULT NULL,
                                      `phone` varchar(255) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `UKrfbvkrffamfql7cjmen8v976v` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- main_categories definition

CREATE TABLE `main_categories` (
                                            `orders` int DEFAULT NULL,
                                            `created_by` bigint DEFAULT NULL,
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `modified_by` bigint DEFAULT NULL,
                                            `name` varchar(255) DEFAULT NULL,
                                            PRIMARY KEY (`id`),
                                            UNIQUE KEY `UKsuhf8pbc9dbe7kq83q97gh85j` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- products definition

CREATE TABLE `products` (
                                     `amount` bigint NOT NULL,
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `seller_id` bigint DEFAULT NULL,
                                     `shop_id` bigint DEFAULT NULL,
                                     `sub_category_id` bigint DEFAULT NULL,
                                     `image_url` varchar(255) DEFAULT NULL,
                                     `name` varchar(255) DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- sellers definition

CREATE TABLE `sellers` (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `address` varchar(255) DEFAULT NULL,
                                    `birth` varchar(255) DEFAULT NULL,
                                    `email` varchar(255) DEFAULT NULL,
                                    `name` varchar(255) DEFAULT NULL,
                                    `password` varchar(255) DEFAULT NULL,
                                    `phone` varchar(255) DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `UK411i31ri1c7c57j5v8q0hyp4d` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- shops definition

CREATE TABLE `shops` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `seller_id` bigint NOT NULL,
                                  `name` varchar(255) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- wish_lists definition

CREATE TABLE `wish_lists` (
                                       `customer_id` bigint DEFAULT NULL,
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `product_id` bigint DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- sub_categories definition

CREATE TABLE `sub_categories` (
                                           `created_by` bigint DEFAULT NULL,
                                           `id` bigint NOT NULL AUTO_INCREMENT,
                                           `main_category_id` bigint DEFAULT NULL,
                                           `modified_by` bigint DEFAULT NULL,
                                           `orders` bigint DEFAULT NULL,
                                           `name` varchar(255) DEFAULT NULL,
                                           PRIMARY KEY (`id`),
                                           UNIQUE KEY `UK5m83dk3c53lrusxi146xmlv6` (`name`),
                                           KEY `FK5aiqtacvp2i3j2ya1fq0bjr3h` (`main_category_id`)
    # DDL 상에서는 FK 를 정의한다.
    # CONSTRAINT `FK5aiqtacvp2i3j2ya1fq0bjr3h` FOREIGN KEY (`main_category_id`) REFERENCES `main_categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;