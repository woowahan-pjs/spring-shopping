package shopping.category.application;

import org.springframework.stereotype.Service;
import shopping.category.application.command.CategoryRegistrationCommand;
import shopping.category.domain.Category;
import shopping.category.domain.CategoryRegistrationRequest;
import shopping.category.domain.CategoryRepository;

@Service
public class CategoryService implements CategoryRegistrationUseCase {

    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category register(final CategoryRegistrationCommand command) {
        return categoryRepository.save(mapToDomain(command));
    }

    private CategoryRegistrationRequest mapToDomain(final CategoryRegistrationCommand command) {
        return new CategoryRegistrationRequest(
                command.name(),
                command.order(),
                command.adminId()
        );
    }
}
