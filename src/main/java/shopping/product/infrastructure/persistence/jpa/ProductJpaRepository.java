package shopping.product.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.product.domain.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByIdAndDeletedFalse(Long id);

    List<ProductEntity> findAllByDeletedFalse();
}
