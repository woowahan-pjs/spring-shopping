package shopping.product.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.domain.repository.ProductRepository;
import shopping.product.infrastructure.persistence.ProductEntity;
import shopping.product.infrastructure.persistence.ProductEntityJpaRepository;

import static shopping.product.infrastructure.ProductEntityMapper.domainToEntity;
import static shopping.product.infrastructure.ProductEntityMapper.entityToDomain;

@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductEntityJpaRepository productEntityJpaRepository;

    public ProductRepositoryAdapter(final ProductEntityJpaRepository productEntityJpaRepository) {
        this.productEntityJpaRepository = productEntityJpaRepository;
    }

    @Transactional
    @Override
    public Product save(final Product product) {
        final ProductEntity productEntity = productEntityJpaRepository.save(domainToEntity(product));
        return entityToDomain(productEntity);
    }
}
