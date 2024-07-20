package shopping.shop.api.dto;

public class ShopRegistrationRequest {
    private String name;

    public ShopRegistrationRequest() {
    }

    public ShopRegistrationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}