package shopping.product.api.query.dto;

import shopping.product.service.dto.ProductOutput;

public record ProductDetailResponse(Long id, String name, Long price, String imageUrl) {

    public static ProductDetailResponse from(ProductOutput output) {
        return new ProductDetailResponse(output.id(), output.name(), output.price(), output.imageUrl());
    }
}
