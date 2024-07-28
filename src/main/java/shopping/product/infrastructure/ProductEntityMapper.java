package shopping.product.infrastructure;

import shopping.product.domain.Product;
import shopping.product.domain.ProductDetailedImage;
import shopping.product.infrastructure.persistence.ProductDetailedImageEntity;
import shopping.product.infrastructure.persistence.ProductEntity;

import java.util.List;

public class ProductEntityMapper {
    private ProductEntityMapper() {
    }

    public static Product entityToDomain(final ProductEntity productEntity) {
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

    public static List<ProductDetailedImage> entityToDomain(final List<ProductDetailedImageEntity> detailedImages) {
        return detailedImages.stream()
                .map(it -> new ProductDetailedImage(it.getId(), it.getDetailedImageUrl()))
                .toList();
    }

    public static ProductEntity domainToEntity(final Product product) {
        return new ProductEntity(
                product.id(),
                product.name(),
                product.amount(),
                product.thumbnailImageUrl(),
                product.subCategoryId(),
                product.shopId(),
                product.sellerId(),
                domainToEntity(product.detailedImageUrls())
        );
    }

    public static List<ProductDetailedImageEntity> domainToEntity(final List<ProductDetailedImage> detailedImageUrls) {
        return detailedImageUrls.stream()
                .map(it -> new ProductDetailedImageEntity(it.id(), it.detailedImageUrl(), null))
                .toList();
    }
}
