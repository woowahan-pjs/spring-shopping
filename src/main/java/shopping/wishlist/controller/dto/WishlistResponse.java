package shopping.wishlist.controller.dto;

import shopping.wishlist.domain.Wishlist;

public record WishlistResponse(Long id, Long productId){

    public static WishlistResponse from(Wishlist item) {
        return new WishlistResponse(item.getId(), item.getProductId());
    }
}
