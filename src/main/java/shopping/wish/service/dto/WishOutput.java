package shopping.wish.service.dto;

import shopping.product.domain.Product;
import shopping.wish.domain.Wish;

public record WishOutput(Long id, Long productId, String productName, Long price, String imageUrl) {

    public static WishOutput of(Wish wish, Product product) {
        return new WishOutput(
                wish.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
}
