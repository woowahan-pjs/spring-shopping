package shopping.product.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreate(
        @NotNull @NotBlank String name,
        @NotNull @NotBlank String imageUrl,
        @NotNull Integer price
) {
}
