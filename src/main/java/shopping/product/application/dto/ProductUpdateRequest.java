package shopping.product.application.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductUpdateRequest(
        @NotBlank
        String name,
        @NotBlank
        Long price,
        String image
) {

}
