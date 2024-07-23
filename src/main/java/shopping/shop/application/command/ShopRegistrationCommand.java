package shopping.shop.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import shopping.common.CommandValidating;

public record ShopRegistrationCommand(
        @NotBlank String name,
        @Min(1) long userId
) implements CommandValidating<ShopRegistrationCommand> {
    public ShopRegistrationCommand(final String name, final long userId) {
        this.name = name;
        this.userId = userId;
        validateSelf(this);
    }
}