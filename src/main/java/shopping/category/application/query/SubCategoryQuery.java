package shopping.category.application.query;

import shopping.category.domain.SubCategory;

public record SubCategoryQuery(Long id, String name, long order, long createdBy, long modifiedBy) {
    public SubCategoryQuery(final SubCategory subCategory) {
        this(subCategory.id(), subCategory.name(), subCategory.order(), subCategory.createdBy(), subCategory.modifiedBy());
    }
}
