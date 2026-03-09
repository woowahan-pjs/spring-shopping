package shopping.product.adapter.in.api;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Integer stockQuantity
) {
}
