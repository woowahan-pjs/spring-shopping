package shopping.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProfanityValidator profanityValidator;

    public ProductService(ProductRepository productRepository, ProfanityValidator profanityValidator) {
        this.productRepository = productRepository;
        this.profanityValidator = profanityValidator;
    }

    public Product save(Product product) {
        validateProductName(product);

        return productRepository.save(product);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다."));
    }

    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product update(Long id, Product product) {
        validateProductName(product);
        return productRepository.update(id, product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    private void validateProductName(Product product) {
        if (profanityValidator.containsProfanity(product.getName())) {
            throw new IllegalArgumentException("비속어가 포함되어 있습니다.");
        }
    }
}
