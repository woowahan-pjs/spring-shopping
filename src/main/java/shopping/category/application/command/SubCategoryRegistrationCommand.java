package shopping.category.application.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import shopping.common.CommandValidating;

public record SubCategoryRegistrationCommand(
        @NotBlank String name,
        @Min(0) int order,
        @Min(1) long mainCategoryId,
        @Min(1) long adminId
) implements CommandValidating<SubCategoryRegistrationCommand> {
    public SubCategoryRegistrationCommand(final String name,
                                          final int order,
                                          final long mainCategoryId,
                                          final long adminId) {
        this.name = name;
        this.order = order;
        this.mainCategoryId = mainCategoryId;
        this.adminId = adminId;
        validateSelf(this);
    }
}

