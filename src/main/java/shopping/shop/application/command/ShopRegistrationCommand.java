package shopping.shop.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import shopping.common.CommandValidating;

public record ShopRegistrationCommand(
        @Min(1) long sellerId,
        @NotBlank String name
) implements CommandValidating<ShopRegistrationCommand> {
    public ShopRegistrationCommand(final long sellerId, final String name) {
        this.sellerId = sellerId;
        this.name = name;
    }
}