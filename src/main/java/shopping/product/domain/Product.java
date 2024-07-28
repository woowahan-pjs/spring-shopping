package shopping.product.domain;

import java.util.List;

public record Product(
        long id,
        String name,
        long amount,
        String thumbnailImageUrl,
        long subCategoryId,
        long shopId,
        long sellerId,
        List<ProductDetailedImage> detailedImages
) {
}
