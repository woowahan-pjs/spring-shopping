package shopping.product.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;

@Transactional(readOnly = true)
@Component
public class ProductReader {
    private final ProductRepository productRepository;

    public ProductReader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product readById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public Page<Product> readAllBy(Pageable pageable) {
        return productRepository.findAllBy(pageable);
    }
}
