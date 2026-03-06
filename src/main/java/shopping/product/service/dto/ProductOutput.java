package shopping.product.service.dto;

import shopping.product.domain.Product;

public record ProductOutput(Long id, String name, Long price, String imageUrl) {

    public static ProductOutput from(Product product) {
        return new ProductOutput(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }
}
