package shopping.category.domain;

import java.util.List;

public record Category(long id, String name, int order, List<SubCategory> subCategories) {
}
