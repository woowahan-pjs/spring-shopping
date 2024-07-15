package shopping.product.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductUpdate(
        @NotBlank String name,
        @NotBlank String imageUrl,
        @NotNull Integer price
) {
}
