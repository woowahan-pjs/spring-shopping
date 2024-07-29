package shopping.category.application;

import shopping.category.application.command.SubCategoryRegistrationCommand;
import shopping.category.application.query.CategoryQuery;

public interface SubCategoryRegistrationUseCase {
    CategoryQuery registerSub(final SubCategoryRegistrationCommand command);
}
