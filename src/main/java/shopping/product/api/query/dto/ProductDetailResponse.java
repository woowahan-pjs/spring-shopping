package shopping.product.api.query.dto;

import shopping.product.domain.Product;

public record ProductDetailResponse(Long id, String name, Long price, String imageUrl) {

    public static ProductDetailResponse from(Product product) {
        return new ProductDetailResponse(product.getId(), product.getName(), product.getPrice().value(), product.getImageUrl());
    }
}
