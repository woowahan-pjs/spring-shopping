package shopping.category.application;

import shopping.category.application.command.SubCategoryRegistrationCommand;
import shopping.category.domain.Category;

public interface SubCategoryRegistrationUseCase {
    Category registerSub(final SubCategoryRegistrationCommand command);
}
