package shopping.category.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CategoryRegistrationCommand(
        @NotBlank String name,
        @Min(0) int order,
        @Min(1) long adminId) {
}
