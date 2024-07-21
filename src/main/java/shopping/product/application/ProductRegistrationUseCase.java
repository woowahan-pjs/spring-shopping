package shopping.product.application;

import shopping.product.application.command.ProductRegistrationCommand;
import shopping.product.domain.Product;

public interface ProductRegistrationUseCase {
    Product register(ProductRegistrationCommand command);
}
