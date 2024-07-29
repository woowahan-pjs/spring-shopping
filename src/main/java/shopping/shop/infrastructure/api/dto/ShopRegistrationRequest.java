package shopping.shop.infrastructure.api.dto;

import shopping.shop.application.command.ShopRegistrationCommand;

public class ShopRegistrationRequest {
    private String name;

    public ShopRegistrationRequest() {
    }

    public ShopRegistrationRequest(final String name) {
        this.name = name;
    }

    public ShopRegistrationCommand toCommand(final long sellerId) {
        return new ShopRegistrationCommand(sellerId, name);
    }

    public String getName() {
        return name;
    }
}