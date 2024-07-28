package shopping.product.domain;

public record ProductRegistrationRequest(
        String name,
        long amount,
        String thumbnailImageUrl,
        long subCategoryId,
        long shopId,
        long sellerId
) {
}
