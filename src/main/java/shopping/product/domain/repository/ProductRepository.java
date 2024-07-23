package shopping.product.domain.repository;

import shopping.product.domain.Product;
import shopping.product.domain.ProductRegistrationRequest;

public interface ProductRepository {
    Product save(final ProductRegistrationRequest productRegistrationRequest);
}
