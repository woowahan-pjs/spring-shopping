package shopping.service;

import org.springframework.stereotype.Service;
import shopping.domain.InMemoryProductRepository;
import shopping.domain.Product;
import shopping.domain.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProfanityValidator profanityValidator;

    public ProductService() {
        this.productRepository = new InMemoryProductRepository();
        this.profanityValidator = new PurgoMalumValidator();
    }

    public Product save(Product product) {
        //비속어 검증.
        if (profanityValidator.containsProfanity(product.getName())) {
            throw new IllegalArgumentException("비속어가 포함되어 있습니다.");
        }

        return productRepository.save(product);
    }
}
