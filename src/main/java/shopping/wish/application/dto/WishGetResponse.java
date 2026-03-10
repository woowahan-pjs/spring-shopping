package shopping.wish.application.dto;

import shopping.product.domain.ProductEntity;
import shopping.wish.domain.WishEntity;

public record WishGetResponse(
    Long wishId,
    Long productId,
    String name,
    int price,
    String imageUrl
) {

    public static WishGetResponse of(WishEntity wish, ProductEntity product) {
        return new WishGetResponse(
            wish.getId(),
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }
}
