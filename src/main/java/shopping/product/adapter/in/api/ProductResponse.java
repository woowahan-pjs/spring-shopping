package shopping.product.adapter.in.api;

import java.math.BigDecimal;
import shopping.product.domain.Product;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Integer stockQuantity
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getStockQuantity()
        );
    }
}
