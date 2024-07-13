package shopping.product.application;

import org.springframework.stereotype.Service;
import shopping.product.domain.Product;
import shopping.product.domain.ProductCreate;
import shopping.product.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void save(final ProductCreate productCreate) {
        Product product = Product.from(productCreate);
        productRepository.save(product);
    }
}
