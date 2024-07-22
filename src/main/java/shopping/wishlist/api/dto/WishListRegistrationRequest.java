package shopping.wishlist.api.dto;

public class WishListRegistrationRequest {
    private long productId;

    public WishListRegistrationRequest() {
    }

    public WishListRegistrationRequest(final long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return productId;
    }
}