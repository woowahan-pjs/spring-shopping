package shopping.category.domain;

import java.util.List;

public record Category(
        Long id,
        String name,
        int order,
        List<SubCategory> subCategories,
        long createdBy,
        long modifiedBy
) {

    public void addSubCategory(final String name, final int order, final long adminId) {
        subCategories.add(new SubCategory(null, name, order, adminId, adminId));
    }
}
