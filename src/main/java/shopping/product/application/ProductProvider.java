package shopping.product.application;

import shopping.product.domain.Product;

public interface ProductProvider {
    Product findProductById(Long id);
}
