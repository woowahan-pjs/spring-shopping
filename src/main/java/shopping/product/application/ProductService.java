package shopping.product.application;

import org.springframework.stereotype.Service;
import shopping.product.application.command.ProductRegistrationCommand;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRegistrationRequest;
import shopping.product.domain.ProductRepository;

@Service
public class ProductService implements ProductRegistrationUseCase {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product register(final ProductRegistrationCommand command) {
        return productRepository.save(mapToDomain(command));
    }

    private ProductRegistrationRequest mapToDomain(final ProductRegistrationCommand command) {
        return new ProductRegistrationRequest(
                command.name(),
                command.amount(),
                command.imageUrl(),
                command.subCategoryId(),
                command.shopId(),
                command.sellerId()
        );
    }
}
