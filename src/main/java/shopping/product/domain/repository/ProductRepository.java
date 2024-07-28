package shopping.product.domain.repository;

import shopping.product.domain.Product;

public interface ProductRepository {
    Product save(final Product product);
}
