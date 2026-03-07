package shopping.product;

public record ProductResponse(Long id, String name, long price, String imageUrl) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl());
    }
}
