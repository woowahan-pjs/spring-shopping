package shopping.product.application.query;

import shopping.product.domain.Product;
import shopping.product.domain.ProductDetailedImage;

import java.util.List;

public record ProductRegistrationQuery(
        long id,
        String name,
        long amount,
        String thumbnailImageUrl,
        long subCategoryId,
        long shopId,
        long sellerId,
        List<String> detailedImageUrls
) {
    public ProductRegistrationQuery(final Product product) {
        this(product.id(), product.name(), product.amount(), product.thumbnailImageUrl(), product.subCategoryId(), product.shopId(), product.sellerId(),
                product.detailedImageUrls().stream().map(ProductDetailedImage::detailedImageUrl).toList());
    }
}
