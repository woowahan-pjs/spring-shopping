package shopping.service;

import org.springframework.stereotype.Service;
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
        //비속어 검증.
        if (profanityValidator.containsProfanity(product.getName())) {
            throw new IllegalArgumentException("비속어가 포함되어 있습니다.");
        }

        return productRepository.save(product);
    }

    public Product findProduct(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findProducts() {
        return productRepository.findAll();
    }
}
