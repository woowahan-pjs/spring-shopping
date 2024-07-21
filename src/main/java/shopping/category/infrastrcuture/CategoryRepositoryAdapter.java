package shopping.category.infrastrcuture;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.category.domain.Category;
import shopping.category.domain.CategoryRegistrationRequest;
import shopping.category.domain.CategoryRepository;
import shopping.category.domain.SubCategory;
import shopping.category.infrastrcuture.persistence.CategoryEntityJpaRepository;
import shopping.category.infrastrcuture.persistence.MainCategoryEntity;
import shopping.category.infrastrcuture.persistence.SubCategoryEntity;

import java.util.ArrayList;

@Component
public class CategoryRepositoryAdapter implements CategoryRepository {
    private final CategoryEntityJpaRepository categoryEntityJpaRepository;

    public CategoryRepositoryAdapter(final CategoryEntityJpaRepository categoryEntityJpaRepository) {
        this.categoryEntityJpaRepository = categoryEntityJpaRepository;
    }

    @Override
    public Category findById(final long mainCategoryId) {
        return categoryEntityJpaRepository.findById(mainCategoryId)
                .map(this::entityToDomain)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional
    @Override
    public Category save(final CategoryRegistrationRequest categoryRegistrationRequest) {
        final MainCategoryEntity mainCategoryEntity = categoryEntityJpaRepository.save(domainToEntity(categoryRegistrationRequest));
        return entityToDomain(mainCategoryEntity);
    }

    @Transactional
    @Override
    public Category save(final Category category) {
        final MainCategoryEntity mainCategoryEntity = categoryEntityJpaRepository.save(domainToEntity(category));
        return entityToDomain(mainCategoryEntity);
    }

    private Category entityToDomain(final MainCategoryEntity mainCategoryEntity) {
        return new Category(
                mainCategoryEntity.getId(),
                mainCategoryEntity.getName(),
                mainCategoryEntity.getOrder(),
                mainCategoryEntity.getCreatedBy(),
                mainCategoryEntity.getModifiedBy(),
                mainCategoryEntity.getSubCategoryEntities().stream()
                        .map(this::entityToDomain)
                        .toList()
        );
    }

    private SubCategory entityToDomain(final SubCategoryEntity subCategoryEntity) {
        return new SubCategory(subCategoryEntity.getId(), subCategoryEntity.getName(), subCategoryEntity.getOrder(), subCategoryEntity.getCreatedBy(), subCategoryEntity.getModifiedBy());
    }

    private MainCategoryEntity domainToEntity(final CategoryRegistrationRequest categoryRegistrationRequest) {
        return new MainCategoryEntity(
                null,
                categoryRegistrationRequest.name(),
                categoryRegistrationRequest.order(),
                categoryRegistrationRequest.adminId(),
                categoryRegistrationRequest.adminId(),
                new ArrayList<>()
        );
    }

    private MainCategoryEntity domainToEntity(final Category category) {
        return new MainCategoryEntity(
                category.id(),
                category.name(),
                category.order(),
                category.createdBy(),
                category.modifiedBy(),
                category.subCategories().stream()
                        .map(this::domainToEntity)
                        .toList()
        );
    }

    private SubCategoryEntity domainToEntity(final SubCategory subCategory) {
        return new SubCategoryEntity(
                subCategory.getId(),
                subCategory.getName(),
                subCategory.getOrder(),
                subCategory.getCreatedBy(),
                subCategory.getModifiedBy()
        );
    }
}
