package shopping.wish.application.dto;

import jakarta.validation.constraints.NotNull;

public record WishAddRequest(
        @NotNull Long productId
) {
}
