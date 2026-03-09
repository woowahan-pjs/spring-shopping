package shopping.product;

import java.util.UUID;

public record ProductResponse(UUID id, String name, long price, String imageUrl) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName().getValue(),
                product.getPrice(), product.getImageUrl());
    }
}
