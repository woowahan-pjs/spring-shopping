package shopping.product.adapter.out;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;
import shopping.product.domain.ProductStatus;
import shopping.product.port.out.ProductSnapshot;
import shopping.product.port.out.ProductSnapshotProvider;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RepositoryProductSnapshotProvider implements ProductSnapshotProvider {
    private final ProductRepository productRepository;

    @Override
    public ProductSnapshot getActiveProduct(Long productId) {
        return findActiveProduct(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public Optional<ProductSnapshot> findActiveProduct(Long productId) {
        return productRepository.findByIdAndStatus(productId, ProductStatus.ACTIVE)
                .map(this::toSnapshot);
    }

    private ProductSnapshot toSnapshot(Product product) {
        return new ProductSnapshot(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
}
