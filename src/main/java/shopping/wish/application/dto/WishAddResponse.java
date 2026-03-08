package shopping.wish.application.dto;

import shopping.wish.domain.WishEntity;

public record WishAddResponse(
        Long wishId,
        Long productId
) {
    public static WishAddResponse from(WishEntity wish) {
        return new WishAddResponse(wish.getId(), wish.getProductId());
    }
}
