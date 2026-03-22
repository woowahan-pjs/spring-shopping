package shopping.wish.dto;

import shopping.wish.domain.Wish;

import java.util.UUID;

import shopping.product.dto.ProductResponse;

public record WishResponse(UUID productId, String name, long price, String imageUrl) {

    public static WishResponse of(Wish wish, ProductResponse product) {
        return new WishResponse(product.id(), product.name(), product.price(), product.imageUrl());
    }
}
