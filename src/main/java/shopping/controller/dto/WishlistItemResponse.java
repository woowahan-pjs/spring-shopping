package shopping.controller.dto;

import shopping.domain.WishlistItem;

public class WishlistItemResponse {
    private Long id;
    private Long productId;

    public WishlistItemResponse() {}

    public WishlistItemResponse(Long id, Long productId) {
        this.id = id;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public static WishlistItemResponse from(WishlistItem item) {
        return new WishlistItemResponse(item.getId(), item.getProductId());
    }
}
