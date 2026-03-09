package shopping.wish.adapter.in.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WishCreateRequest(
        @NotNull Long productId,
        @Min(1) Integer quantity
) {
}
