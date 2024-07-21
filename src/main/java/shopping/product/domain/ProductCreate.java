package shopping.product.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreate(
        @NotBlank String name,
        @NotBlank String imageUrl,
        @NotNull Integer price
) {
}
