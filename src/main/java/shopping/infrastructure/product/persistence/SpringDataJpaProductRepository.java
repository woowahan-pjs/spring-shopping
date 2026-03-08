package shopping.infrastructure.product.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.domain.product.Product;

public interface SpringDataJpaProductRepository extends JpaRepository<Product, Long> {
}
