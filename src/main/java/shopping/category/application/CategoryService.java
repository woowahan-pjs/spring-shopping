package shopping.category.application;

import org.springframework.stereotype.Service;
import shopping.category.application.command.CategoryRegistrationCommand;
import shopping.category.application.command.SubCategoryRegistrationCommand;
import shopping.category.application.query.CategoryQuery;
import shopping.category.domain.Category;
import shopping.category.domain.CategoryRegistrationRequest;
import shopping.category.domain.repository.CategoryRepository;

@Service
public class CategoryService implements CategoryRegistrationUseCase, SubCategoryRegistrationUseCase {

    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryQuery register(final CategoryRegistrationCommand command) {
        final Category category = categoryRepository.save(mapToDomain(command));
        return new CategoryQuery(category);
    }

    private CategoryRegistrationRequest mapToDomain(final CategoryRegistrationCommand command) {
        return new CategoryRegistrationRequest(
                command.name(),
                command.order(),
                command.adminId()
        );
    }

    @Override
    public CategoryQuery registerSub(final SubCategoryRegistrationCommand command) {
        final Category category = categoryRepository.findById(command.mainCategoryId());
        category.addSubCategory(command.name(), command.order(), command.adminId());
        return new CategoryQuery(categoryRepository.save(category));
    }
}
