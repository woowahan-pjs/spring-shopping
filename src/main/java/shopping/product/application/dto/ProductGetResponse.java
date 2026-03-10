package shopping.product.application.dto;

import shopping.product.domain.ProductEntity;

public record ProductGetResponse(
    Long id,
    String name,
    int price,
    String imageUrl
) {

    public static ProductGetResponse from(ProductEntity product) {
        return new ProductGetResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl());
    }
}
