package shopping.product.domain;

public record ProductDetails(
        ProductName name,
        String description,
        ProductImageUrl imageUrl,
        ProductPrice price
) {
    public ProductDetails {
        description = normalizeDescription(description);
    }

    private static String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }
        return description;
    }
}
