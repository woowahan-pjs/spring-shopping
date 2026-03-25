package shopping.product.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndStatus(Long id, ProductStatus status);

    List<Product> findByStatusOrderByIdAsc(ProductStatus status);
}
