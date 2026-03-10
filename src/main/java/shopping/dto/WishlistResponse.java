package shopping.dto;

import java.math.BigDecimal;

public record WishlistResponse(
    Long id,
    Long productId,
    String productName,
    BigDecimal price
) {
}
