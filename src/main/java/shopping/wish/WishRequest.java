package shopping.wish;

import jakarta.validation.constraints.NotNull;

public record WishRequest(@NotNull Long productId) {
}
