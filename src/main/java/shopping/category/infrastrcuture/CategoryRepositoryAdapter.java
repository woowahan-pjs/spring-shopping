package shopping.category.infrastrcuture;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.category.domain.Category;
import shopping.category.domain.repository.CategoryRepository;
import shopping.category.infrastrcuture.persistence.CategoryEntityJpaRepository;
import shopping.category.infrastrcuture.persistence.MainCategoryEntity;

import static shopping.category.infrastrcuture.CategoryEntityMapper.domainToEntity;
import static shopping.category.infrastrcuture.CategoryEntityMapper.entityToDomain;

@Component
public class CategoryRepositoryAdapter implements CategoryRepository {
    private final CategoryEntityJpaRepository categoryEntityJpaRepository;

    public CategoryRepositoryAdapter(final CategoryEntityJpaRepository categoryEntityJpaRepository) {
        this.categoryEntityJpaRepository = categoryEntityJpaRepository;
    }

    @Transactional
    @Override
    public Category save(final Category category) {
        final MainCategoryEntity mainCategoryEntity = categoryEntityJpaRepository.save(domainToEntity(category));
        return entityToDomain(mainCategoryEntity);
    }

    @Override
    public Category findById(final long mainCategoryId) {
        return categoryEntityJpaRepository.findById(mainCategoryId)
                .map(CategoryEntityMapper::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException());
    }

}
