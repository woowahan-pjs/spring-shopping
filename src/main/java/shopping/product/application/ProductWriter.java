package shopping.product.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.domain.ProductUpdate;

@Component
public class ProductWriter {
    private final ProductRepository productRepository;

    public ProductWriter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void write(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void update(Long id, ProductUpdate productUpdate) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        product.update(productUpdate);
    }
}
