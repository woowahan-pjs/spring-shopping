package shopping.wish.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WishCreateRequest(
        @NotNull Long productId,
        @Min(1) Integer quantity
) {
}
