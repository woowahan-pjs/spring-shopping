package shopping.wishlist.controller.dto;

import shopping.wishlist.domain.Wishlist;

public class WishlistResponse {
    private Long id;
    private Long productId;

    public WishlistResponse() {}

    public WishlistResponse(Long id, Long productId) {
        this.id = id;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public static WishlistResponse from(Wishlist item) {
        return new WishlistResponse(item.getId(), item.getProductId());
    }
}
