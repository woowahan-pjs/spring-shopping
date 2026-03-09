package shopping.wish.service.dto;

import shopping.wish.domain.Wish;
import shopping.product.service.dto.ProductOutput;

public record WishAddOutput(Long id, Long productId, String productName) {

    public static WishAddOutput of(Wish wish, ProductOutput product) {
        return new WishAddOutput(wish.getId(), product.id(), product.name());
    }
}
