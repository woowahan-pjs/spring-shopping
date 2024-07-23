package shopping.wishlist.application.command;

import jakarta.validation.constraints.Min;
import shopping.common.CommandValidating;

public record WishListRegistrationCommand(
        @Min(1) long productId,
        @Min(1) long customerId
) implements CommandValidating<WishListRegistrationCommand> {
    public WishListRegistrationCommand(final long productId, final long customerId) {
        this.productId = productId;
        this.customerId = customerId;
        validateSelf(this);
    }
}
