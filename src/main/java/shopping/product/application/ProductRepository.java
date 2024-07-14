package shopping.product.application;

import java.util.Optional;
import shopping.product.domain.Product;

public interface ProductRepository {
    void save(Product product);

    Optional<Product> findById(Long id);
}
