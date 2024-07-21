package shopping.product.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRegistrationRequest;
import shopping.product.domain.ProductRepository;
import shopping.product.infrastructure.persistence.ProductEntity;
import shopping.product.infrastructure.persistence.ProductEntityJpaRepository;

@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductEntityJpaRepository productEntityJpaRepository;

    public ProductRepositoryAdapter(final ProductEntityJpaRepository productEntityJpaRepository) {
        this.productEntityJpaRepository = productEntityJpaRepository;
    }

    @Transactional
    @Override
    public Product save(final ProductRegistrationRequest productRegistrationRequest) {
        final ProductEntity productEntity = productEntityJpaRepository.save(domainToEntity(productRegistrationRequest));
        return entityToDomain(productEntity);
    }

    private Product entityToDomain(final ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getAmount(),
                productEntity.getImageUrl(),
                productEntity.getSubCategoryId(),
                productEntity.getShopId(),
                productEntity.getSellerId()
        );
    }

    private ProductEntity domainToEntity(final ProductRegistrationRequest productRegistrationRequest) {
        return new ProductEntity(
                null,
                productRegistrationRequest.name(),
                productRegistrationRequest.amount(),
                productRegistrationRequest.imageUrl(),
                productRegistrationRequest.subCategoryId(),
                productRegistrationRequest.shopId(),
                productRegistrationRequest.sellerId()
        );
    }
}
