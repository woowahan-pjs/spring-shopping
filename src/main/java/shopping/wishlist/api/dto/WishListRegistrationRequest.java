package shopping.wishlist.api.dto;

import shopping.wishlist.application.command.WishListRegistrationCommand;

public class WishListRegistrationRequest {
    private long productId;

    public WishListRegistrationRequest() {
    }

    public WishListRegistrationRequest(final long productId) {
        this.productId = productId;
    }

    public WishListRegistrationCommand toCommand(final long customerId) {
        return new WishListRegistrationCommand(productId, customerId);
    }

    public long getProductId() {
        return productId;
    }
}