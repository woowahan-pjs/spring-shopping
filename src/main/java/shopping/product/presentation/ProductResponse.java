package shopping.product.presentation;

import shopping.product.domain.Product;

public record ProductResponse(Long id, String name, String imageUrl, Integer price) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice()
        );
    }
}
