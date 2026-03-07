package shopping.product.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    ProductEntity save(ProductEntity product);
    Optional<ProductEntity> findByIdNotDeleted(Long id);
    List<ProductEntity> findAllNotDeleted();
}
