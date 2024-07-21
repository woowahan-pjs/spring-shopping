package shopping.category.application;

import shopping.category.application.command.CategoryRegistrationCommand;
import shopping.category.domain.Category;

public interface CategoryRegistrationUseCase {
    Category register(final CategoryRegistrationCommand command);
}
