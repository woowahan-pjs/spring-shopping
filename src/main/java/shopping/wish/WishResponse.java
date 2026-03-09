package shopping.wish;

import java.util.UUID;

import shopping.product.Product;

public record WishResponse(UUID productId, String name, long price, String imageUrl) {

    public static WishResponse of(Wish wish, Product product) {
        return new WishResponse(product.getId(), product.getName().getValue(), product.getPrice(),
                product.getImageUrl());
    }
}
