package shopping.category.domain;

import java.util.List;

public record Category(
        long id,
        String name,
        int order,
        long createdBy,
        long modifiedBy,
        List<SubCategory> subCategories
) {

    public void addSubCategory(final String name, final int order, final long adminId) {
        subCategories.add(new SubCategory(null, name, order, adminId, adminId));
    }
}
