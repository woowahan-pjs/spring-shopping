package shopping.product.controller.dto;

import shopping.product.domain.Product;

import java.math.BigDecimal;

public record ProductRequest(String name, BigDecimal price, String imageUrl) {
    public Product toProduct() {
        return new Product(name, price, imageUrl);
    }
}
