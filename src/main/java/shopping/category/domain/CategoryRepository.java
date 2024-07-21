package shopping.category.domain;

public interface CategoryRepository {
    Category save(CategoryRegistrationRequest categoryRegistrationRequest);
    Category save(Category category);
    Category findById(long mainCategoryId);
}
