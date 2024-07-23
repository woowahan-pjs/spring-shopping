package shopping.category.domain.repository;

import shopping.category.domain.Category;
import shopping.category.domain.CategoryRegistrationRequest;

public interface CategoryRepository {
    Category save(CategoryRegistrationRequest categoryRegistrationRequest);
    Category save(Category category);
    Category findById(long mainCategoryId);
}
