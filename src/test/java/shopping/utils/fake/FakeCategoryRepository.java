package shopping.utils.fake;

import shopping.category.domain.Category;
import shopping.category.domain.repository.CategoryRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakeCategoryRepository implements CategoryRepository {
    private final Map<Long, Category> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Category save(final Category category) {
        if (Objects.isNull(category.id())) {
            final var id = idGenerator.incrementAndGet();
            final var newCategory = new Category(
                    id,
                    category.name(),
                    category.order(),
                    category.subCategories(),
                    category.createdBy(),
                    category.modifiedBy()
            );
            storage.put(id, newCategory);
            return newCategory;
        }
        return storage.put(category.id(), category);
    }

    @Override
    public Category findById(final long mainCategoryId) {
        return storage.get(mainCategoryId);
    }
}
