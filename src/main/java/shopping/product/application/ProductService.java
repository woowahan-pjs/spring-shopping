package shopping.product.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shopping.product.domain.Product;
import shopping.product.domain.ProductCreate;
import shopping.product.domain.ProductUpdate;

@Service
public class ProductService {
    private final ProductWriter productWriter;
    private final ProductReader productReader;
    private final ProfanityChecker profanityChecker;

    public ProductService(ProductWriter productWriter, ProductReader productReader, ProfanityChecker profanityChecker) {
        this.productWriter = productWriter;
        this.productReader = productReader;
        this.profanityChecker = profanityChecker;
    }

    public void create(ProductCreate productCreate) {
        validateContainsProfanity(productCreate.name());
        Product product = Product.from(productCreate);
        productWriter.write(product);
    }

    private void validateContainsProfanity(String value) {
        if (profanityChecker.check(value)) {
            throw new ContainsProfanityException();
        }
    }

    public Product getById(Long id) {
        return productReader.readById(id);
    }

    public Page<Product> getAllBy(Pageable pageable) {
        return productReader.readAllBy(pageable);
    }

    public void update(Long id, ProductUpdate productUpdate) {
        validateContainsProfanity(productUpdate.name());
        productWriter.update(id, productUpdate);
    }
}
