package shopping.category.application.query;

import shopping.category.domain.Category;

import java.util.List;

public record CategoryQuery(
        long id,
        String name,
        int order,
        long createdBy,
        long modifiedBy,
        List<SubCategoryQuery> subCategories
) {
    public CategoryQuery(final Category category) {
        this(
                category.id(),
                category.name(),
                category.order(),
                category.createdBy(),
                category.modifiedBy(),
                category.subCategories().stream().map(SubCategoryQuery::new).toList()
        );
    }
}
