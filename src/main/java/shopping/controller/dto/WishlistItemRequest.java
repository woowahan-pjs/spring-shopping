package shopping.controller.dto;

public class WishlistItemRequest {
    private Long productId;

    public WishlistItemRequest() {
    }

    public WishlistItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
