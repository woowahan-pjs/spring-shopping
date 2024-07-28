package shopping.product.application.query;

import shopping.product.domain.Product;

public record ProductRegistrationQuery(
        long id,
        String name,
        long amount,
        String thumbnailImageUrl,
        long subCategoryId,
        long shopId,
        long sellerId
) {
    public ProductRegistrationQuery(final Product product) {
        this(product.id(), product.name(), product.amount(), product.thumbnailImageUrl(), product.subCategoryId(), product.shopId(), product.sellerId());
    }
}
