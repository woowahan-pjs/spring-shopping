package shopping.product.port.out;

import java.math.BigDecimal;

public record ProductSnapshot(
        Long productId,
        String productName,
        BigDecimal productPrice,
        String productImageUrl
) {
}
