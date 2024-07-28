package shopping.product.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductUpdateRequest(
        @NotNull
        Long id,
        @NotBlank
        String name,
        @NotBlank
        Long price,
        String image
) {

}
