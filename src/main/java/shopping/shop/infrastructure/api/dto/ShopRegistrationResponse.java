package shopping.shop.infrastructure.api.dto;

public class ShopRegistrationResponse {
    private long id;

    public ShopRegistrationResponse() {
    }

    public ShopRegistrationResponse(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
