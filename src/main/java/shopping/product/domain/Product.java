package shopping.product.domain;

import java.util.List;

public record Product(
        Long id,
        String name,
        long amount,
        String thumbnailImageUrl,
        long subCategoryId,
        long shopId,
        long sellerId,
        List<ProductDetailedImage> detailedImages
) {
}
