package shopping.shop.infrastructure.api.dto;

import shopping.shop.application.command.ShopRegistrationCommand;

public class ShopRegistrationRequest {
    private String name;

    public ShopRegistrationRequest() {
    }

    public ShopRegistrationRequest(final String name) {
        this.name = name;
    }

    public ShopRegistrationCommand toCommand(final long userId) {
        return new ShopRegistrationCommand(name, userId);
    }

    public String getName() {
        return name;
    }
}