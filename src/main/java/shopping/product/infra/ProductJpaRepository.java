package shopping.product.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.product.domain.Product;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
}
