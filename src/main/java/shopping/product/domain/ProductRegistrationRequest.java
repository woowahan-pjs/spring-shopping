package shopping.product.domain;

public record ProductRegistrationRequest(
        String name,
        long amount,
        String imageUrl,
        long subCategoryId,
        long shopId,
        long sellerId
) {
}
