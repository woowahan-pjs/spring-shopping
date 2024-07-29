package shopping.category.application;

import shopping.category.application.command.CategoryRegistrationCommand;
import shopping.category.application.query.CategoryQuery;

public interface CategoryRegistrationUseCase {
    CategoryQuery register(final CategoryRegistrationCommand command);
}
