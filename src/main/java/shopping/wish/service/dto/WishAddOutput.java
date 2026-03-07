package shopping.wish.service.dto;

import shopping.wish.domain.Wish;
import shopping.product.domain.Product;

public record WishAddOutput(Long id, Long productId, String productName) {

    public static WishAddOutput of(Wish wish, Product product) {
        return new WishAddOutput(wish.getId(), product.getId(), product.getName());
    }
}