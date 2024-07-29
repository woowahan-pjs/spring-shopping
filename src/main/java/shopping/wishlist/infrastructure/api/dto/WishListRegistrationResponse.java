package shopping.wishlist.infrastructure.api.dto;

public class WishListRegistrationResponse {
    private long id;

    public WishListRegistrationResponse() {
    }

    public WishListRegistrationResponse(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
