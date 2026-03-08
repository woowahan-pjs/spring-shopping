package shopping.service;

import shopping.domain.Product;
import shopping.domain.ProductRepository;

import java.util.List;

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
        return productRepository.findById(id);
    }

    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    public Product update(Product product) {
        validateProductName(product);
        return productRepository.update(product.getId(), product);
    }

    private void validateProductName(Product product) {
        if (profanityValidator.containsProfanity(product.getName())) {
            throw new IllegalArgumentException("비속어가 포함되어 있습니다.");
        }
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
