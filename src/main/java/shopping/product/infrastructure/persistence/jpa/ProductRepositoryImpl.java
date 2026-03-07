package shopping.product.infrastructure.persistence.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shopping.product.domain.ProductEntity;
import shopping.product.domain.ProductRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public ProductEntity save(ProductEntity product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Optional<ProductEntity> findByIdNotDeleted(Long id) {
        return productJpaRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<ProductEntity> findAllNotDeleted() {
        return productJpaRepository.findAllByDeletedFalse();
    }
}
