package shopping.product.application.query;

import shopping.product.domain.Product;

public record ProductRegistrationQuery(
        long id,
        String name,
        long amount,
        String imageUrl,
        long subCategoryId,
        long shopId,
        long sellerId
) {
    public ProductRegistrationQuery(final Product product) {
        this(product.id(), product.name(), product.amount(), product.imageUrl(), product.subCategoryId(), product.shopId(), product.sellerId());
    }
}
