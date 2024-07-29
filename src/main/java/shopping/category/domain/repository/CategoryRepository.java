package shopping.category.domain.repository;

import shopping.category.domain.Category;

public interface CategoryRepository {
    Category save(Category category);

    Category findById(long mainCategoryId);
}
