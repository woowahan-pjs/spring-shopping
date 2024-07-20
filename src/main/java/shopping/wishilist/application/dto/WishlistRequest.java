package shopping.wishilist.application.dto;

public class WishlistRequest {

    private Long productId;

    public WishlistRequest() {
    }

    public WishlistRequest(final Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
