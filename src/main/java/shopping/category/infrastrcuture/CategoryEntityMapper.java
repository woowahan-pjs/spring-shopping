package shopping.category.infrastrcuture;

import shopping.category.domain.Category;
import shopping.category.domain.SubCategory;
import shopping.category.infrastrcuture.persistence.MainCategoryEntity;
import shopping.category.infrastrcuture.persistence.SubCategoryEntity;

import java.util.stream.Collectors;

public class CategoryEntityMapper {
    private CategoryEntityMapper() {
    }

    public static Category entityToDomain(final MainCategoryEntity category) {
        return new Category(
                category.getId(),
                category.getName(),
                category.getOrder(),
                category.getSubCategoryEntities().stream()
                        .map(CategoryEntityMapper::entityToDomain)
                        .collect(Collectors.toList()),
                category.getCreatedBy(),
                category.getModifiedBy()
        );
    }

    public static SubCategory entityToDomain(final SubCategoryEntity subCategoryEntity) {
        return new SubCategory(subCategoryEntity.getId(), subCategoryEntity.getName(), subCategoryEntity.getOrder(), subCategoryEntity.getCreatedBy(), subCategoryEntity.getModifiedBy());
    }

    public static MainCategoryEntity domainToEntity(final Category category) {
        return new MainCategoryEntity(
                category.id(),
                category.name(),
                category.order(),
                category.createdBy(),
                category.modifiedBy(),
                category.subCategories().stream()
                        .map(CategoryEntityMapper::domainToEntity)
                        .collect(Collectors.toList())
        );
    }

    public static SubCategoryEntity domainToEntity(final SubCategory subCategory) {
        return new SubCategoryEntity(
                subCategory.id(),
                subCategory.name(),
                subCategory.order(),
                subCategory.createdBy(),
                subCategory.modifiedBy()
        );
    }
}
