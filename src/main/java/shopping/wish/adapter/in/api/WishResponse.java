package shopping.wish.adapter.in.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import shopping.product.port.out.ProductSnapshot;
import shopping.wish.domain.WishlistItem;

public record WishResponse(
        Long wishId,
        Long productId,
        String productName,
        BigDecimal productPrice,
        String productImageUrl,
        Integer quantity,
        LocalDateTime addedAt
) {
    public static WishResponse from(WishlistItem item, ProductSnapshot product) {
        return new WishResponse(
                item.getId(),
                product.productId(),
                product.productName(),
                product.productPrice(),
                product.productImageUrl(),
                item.getQuantity(),
                item.getAddedAt()
        );
    }
}
