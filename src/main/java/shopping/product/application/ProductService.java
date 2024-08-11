package shopping.product.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.product.application.dto.ProductCreateRequest;
import shopping.product.application.dto.ProductUpdateRequest;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;
import shopping.product.domain.ProfanityChecker;
import shopping.product.exception.NotFoundProductException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProfanityChecker profanityChecker;
    private final ProductRepository productRepository;

    public Long create(ProductCreateRequest request) {
        final Product product = new Product(request.name(),
                profanityChecker,
                request.price(),
                request.image());
        final Product saved = productRepository.save(product);
        return saved.getId();
    }

    public void update(Long productId, ProductUpdateRequest request) {
        final Product product = findProduct(productId);
        product.update(request.name(),
                profanityChecker,
                request.price(),
                request.image());
        productRepository.save(product);
    }

    public void delete(Long id) {
        final Product product = findProduct(id);
        productRepository.delete(product);
    }

    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException("해당 상품을 찾을 수 없습니다. " + id));
    }

    public List<Product> findProducts(List<Long> ids) {
        return productRepository.findByIdIn(ids);
    }
}
