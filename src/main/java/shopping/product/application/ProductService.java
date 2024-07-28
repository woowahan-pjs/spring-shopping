package shopping.product.application;

import org.springframework.stereotype.Service;
import shopping.product.application.command.ProductRegistrationCommand;
import shopping.product.application.query.ProductRegistrationQuery;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRegistrationRequest;
import shopping.product.domain.repository.ProductRepository;
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
    public ProductRegistrationQuery register(final ProductRegistrationCommand command) {
        productValidator.verify(command);
        final Product product = productRepository.save(mapToDomain(command));
        return new ProductRegistrationQuery(product);
    }

    private ProductRegistrationRequest mapToDomain(final ProductRegistrationCommand command) {
        return new ProductRegistrationRequest(
                command.name(),
                command.amount(),
                command.thumbnailImageUrl(),
                command.subCategoryId(),
                command.shopId(),
                command.sellerId()
        );
    }
}
