package shopping.product.domain;

public record Product(
        long id,
        String name,
        long amount,
        String thumbnailImageUrl,
        long subCategoryId,
        long shopId,
        long sellerId
) {
}
