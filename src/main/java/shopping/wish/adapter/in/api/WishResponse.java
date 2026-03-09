package shopping.wish.adapter.in.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WishResponse(
        Long wishId,
        Long productId,
        String productName,
        BigDecimal productPrice,
        String productImageUrl,
        Integer quantity,
        LocalDateTime addedAt
) {
}
