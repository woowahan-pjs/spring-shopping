package shopping.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
