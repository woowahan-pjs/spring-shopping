package shopping.product.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(
        @Size(max = 15) @Pattern(regexp = "^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]*$") String name,
        @Min(0) Integer price,
        String imageUrl
) {
}
