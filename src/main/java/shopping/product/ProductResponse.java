package shopping.product;

public record ProductResponse(
    Long id,
    String name,
    int price,
    String imageUrl,
    Long categoryId
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId()
        );
    }
}
