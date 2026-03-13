package shopping.wishlist.controller.dto;

public class WishlistRequest {
    private Long productId;

    public WishlistRequest() {
    }

    public WishlistRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
