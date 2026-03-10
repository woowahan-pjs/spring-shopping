package shopping.product.application.dto;

import shopping.product.domain.ProductEntity;

public record ProductCreateResponse(
    Long id,
    String name,
    int price,
    String imageUrl
) {

    public static ProductCreateResponse from(ProductEntity product) {
        return new ProductCreateResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl());
    }
}
