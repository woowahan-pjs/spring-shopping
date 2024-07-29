package shopping.product.application;

import shopping.product.application.command.ProductRegistrationCommand;
import shopping.product.application.query.ProductRegistrationQuery;

public interface ProductRegistrationUseCase {
    ProductRegistrationQuery register(ProductRegistrationCommand command);
}
