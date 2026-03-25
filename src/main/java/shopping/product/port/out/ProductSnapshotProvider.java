package shopping.product.port.out;

import java.util.Optional;

public interface ProductSnapshotProvider {
    ProductSnapshot getActiveProduct(Long productId);

    Optional<ProductSnapshot> findActiveProduct(Long productId);
}
