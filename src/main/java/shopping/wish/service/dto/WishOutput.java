package shopping.wish.service.dto;

import shopping.product.service.dto.ProductOutput;
import shopping.wish.domain.Wish;

public record WishOutput(Long id, Long productId, String productName, Long price, String imageUrl) {
    private static final String DELETED_PRODUCT_NAME = "삭제된 상품입니다.";

    public static WishOutput of(Wish wish, ProductOutput product) {
        return new WishOutput(
                wish.getId(),
                product.id(),
                product.name(),
                product.price(),
                product.imageUrl()
        );
    }

    public static WishOutput deleted(Wish wish) {
        return new WishOutput(
                wish.getId(),
                wish.getProductId(),
                DELETED_PRODUCT_NAME,
                null,
                null
        );
    }
}
