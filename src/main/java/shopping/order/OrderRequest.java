package shopping.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotNull Long optionId,
    @Min(1) int quantity,
    String message
) {
}
