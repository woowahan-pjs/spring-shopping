package shopping.product.application;

import org.springframework.stereotype.Service;
import shopping.product.domain.Product;
import shopping.product.domain.ProductCreate;
import shopping.product.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProfanityCheckService profanityCheckService;

    public ProductService(final ProductRepository productRepository, ProfanityCheckService profanityCheckService) {
        this.productRepository = productRepository;
        this.profanityCheckService = profanityCheckService;
    }

    public void save(final ProductCreate productCreate) {
        validateContainsProfanity(productCreate.name());
        Product product = Product.from(productCreate);
        productRepository.save(product);
    }

    private void validateContainsProfanity(String value) {
        if (profanityCheckService.containsProfanity(value)) {
            throw new ContainsProfanityException();
        }
    }
}
