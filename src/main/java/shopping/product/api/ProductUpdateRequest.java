package shopping.product.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductUpdateRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin(value = "0.01") BigDecimal price,
        @NotBlank String imageUrl
) {
}
