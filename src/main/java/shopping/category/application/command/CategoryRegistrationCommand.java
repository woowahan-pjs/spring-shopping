package shopping.category.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import shopping.common.CommandValidating;

public record CategoryRegistrationCommand(
        @NotBlank String name,
        @Min(0) int order,
        @Min(1) long adminId
) implements CommandValidating<CategoryRegistrationCommand> {

    public CategoryRegistrationCommand(final String name, final int order, final long adminId) {
        this.name = name;
        this.order = order;
        this.adminId = adminId;
        validateSelf(this);
    }
}
