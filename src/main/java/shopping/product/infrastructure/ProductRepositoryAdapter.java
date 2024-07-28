package shopping.product.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.domain.ProductDetailedImage;
import shopping.product.domain.ProductRegistrationRequest;
import shopping.product.domain.repository.ProductRepository;
import shopping.product.infrastructure.persistence.ProductDetailedImageEntity;
import shopping.product.infrastructure.persistence.ProductEntity;
import shopping.product.infrastructure.persistence.ProductEntityJpaRepository;

import java.util.List;

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
                productEntity.getThumbnailImageUrl(),
                productEntity.getSubCategoryId(),
                productEntity.getShopId(),
                productEntity.getSellerId(),
                entityToDomain(productEntity.getDetailedImages())
            );
    }

    private List<ProductDetailedImage> entityToDomain(final List<ProductDetailedImageEntity> detailedImages) {
        return detailedImages.stream()
                .map(it -> new ProductDetailedImage(it.getId(), it.getDetailedImageUrl()))
                .toList();
    }

    private ProductEntity domainToEntity(final ProductRegistrationRequest productRegistrationRequest) {
        return new ProductEntity(
                null,
                productRegistrationRequest.name(),
                productRegistrationRequest.amount(),
                productRegistrationRequest.thumbnailImageUrl(),
                productRegistrationRequest.subCategoryId(),
                productRegistrationRequest.shopId(),
                productRegistrationRequest.sellerId(),
                domainToEntity(productRegistrationRequest.detailedImageUrls())
        );
    }

    private List<ProductDetailedImageEntity> domainToEntity(final List<String> detailedImageUrls) {
        return detailedImageUrls.stream()
                .map(it -> new ProductDetailedImageEntity(null, it, null))
                .toList();
    }
}
