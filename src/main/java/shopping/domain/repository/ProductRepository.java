package shopping.domain.repository;

import shopping.domain.product.Product;

import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
}
