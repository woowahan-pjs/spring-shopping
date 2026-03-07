package shopping.product.application.dto;

import shopping.product.domain.ProductEntity;

public record ProductUpdateResponse(
    Long id,
    String name,
    int price,
    String imageUrl
) {


    public static ProductUpdateResponse from(ProductEntity product) {
        return new ProductUpdateResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl());
    }
}
