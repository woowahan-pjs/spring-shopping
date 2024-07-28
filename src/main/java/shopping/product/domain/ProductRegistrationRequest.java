package shopping.product.domain;

import java.util.List;

public record ProductRegistrationRequest(
        String name,
        long amount,
        String thumbnailImageUrl,
        long subCategoryId,
        long shopId,
        long sellerId,
        List<String> detailedImageUrls
) {
}
