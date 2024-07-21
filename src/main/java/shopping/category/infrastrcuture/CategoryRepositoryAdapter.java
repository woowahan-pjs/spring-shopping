package shopping.category.infrastrcuture;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.category.domain.Category;
import shopping.category.domain.CategoryRegistrationRequest;
import shopping.category.domain.CategoryRepository;
import shopping.category.domain.SubCategory;
import shopping.category.infrastrcuture.persistence.MainCategoryEntity;
import shopping.category.infrastrcuture.persistence.CategoryEntityJpaRepository;
import shopping.category.infrastrcuture.persistence.SubCategoryEntity;

@Component
public class CategoryRepositoryAdapter implements CategoryRepository {
    private final CategoryEntityJpaRepository categoryEntityJpaRepository;

    public CategoryRepositoryAdapter(final CategoryEntityJpaRepository categoryEntityJpaRepository) {
        this.categoryEntityJpaRepository = categoryEntityJpaRepository;
    }

    @Transactional
    @Override
    public Category save(final CategoryRegistrationRequest categoryRegistrationRequest) {
        final MainCategoryEntity mainCategoryEntity = categoryEntityJpaRepository.save(domainToEntity(categoryRegistrationRequest));
        return entityToDomain(mainCategoryEntity);
    }

    private Category entityToDomain(final MainCategoryEntity mainCategoryEntity) {
        return new Category(
                mainCategoryEntity.getId(),
                mainCategoryEntity.getName(),
                mainCategoryEntity.getOrder(),
                mainCategoryEntity.getSubCategoryEntities().stream()
                        .map(this::entityToDomain)
                        .toList()
        );
    }

    private SubCategory entityToDomain(final SubCategoryEntity subCategoryEntity) {
        return new SubCategory(subCategoryEntity.getId());
    }

    private MainCategoryEntity domainToEntity(final CategoryRegistrationRequest categoryRegistrationRequest) {
        return new MainCategoryEntity(
                categoryRegistrationRequest.name(),
                categoryRegistrationRequest.order(),
                categoryRegistrationRequest.adminId(),
                categoryRegistrationRequest.adminId()
        );
    }
}
