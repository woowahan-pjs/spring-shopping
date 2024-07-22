package shopping.product.application;

import org.springframework.stereotype.Service;
import shopping.product.application.command.ProductRegistrationCommand;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRegistrationRequest;
import shopping.product.domain.ProductRepository;
import shopping.product.domain.ProductValidator;

@Service
public class ProductService implements ProductRegistrationUseCase {
    private final ProductValidator productValidator;
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository, final ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
    }

    @Override
    public Product register(final ProductRegistrationCommand command) {
        productValidator.verify(command);
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
