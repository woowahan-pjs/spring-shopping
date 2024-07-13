package shopping.product.application;

import shopping.product.domain.Product;

public interface ProductRepository {
    void save(Product product);
}
