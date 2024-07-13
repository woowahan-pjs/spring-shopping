package shopping.product.application;

import org.springframework.stereotype.Service;
import shopping.product.domain.Product;
import shopping.product.domain.ProductCreate;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProfanityChecker profanityChecker;

    public ProductService(final ProductRepository productRepository, ProfanityChecker profanityChecker) {
        this.productRepository = productRepository;
        this.profanityChecker = profanityChecker;
    }

    public void create(final ProductCreate productCreate) {
        validateContainsProfanity(productCreate.name());
        Product product = Product.from(productCreate);
        productRepository.save(product);
    }

    private void validateContainsProfanity(String value) {
        if (profanityChecker.check(value)) {
            throw new ContainsProfanityException();
        }
    }
}
