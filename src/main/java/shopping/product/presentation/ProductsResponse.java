package shopping.product.presentation;

import java.util.List;
import org.springframework.data.domain.Page;
import shopping.product.domain.Product;

public record ProductsResponse(List<ProductResponse> products, int totalPages, long totalElements) {
    public static ProductsResponse from(Page<Product> products) {
        List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::from)
                .toList();
        return new ProductsResponse(productResponses, products.getTotalPages(), products.getTotalElements());
    }
}
