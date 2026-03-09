package shopping.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeletedFalse(Long id);

    List<Product> findAllByDeletedFalse();
}
