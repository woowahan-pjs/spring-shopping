package shopping.option;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OptionRequest(
    @NotBlank String name,
    @Min(1) @Max(99_999_999) int quantity
) {
}
