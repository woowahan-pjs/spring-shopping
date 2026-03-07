package shopping.product.service;

import shopping.product.domain.Product;
import shopping.product.dto.ProductCreateRequest;
import shopping.product.repository.ProductRepository;
import shopping.product.validator.ProductNameValidator;

public class ProductService {

    private final ProductNameValidator productNameValidator;
    private final ProductRepository productRepository;

    public ProductService(ProductNameValidator productNameValidator, ProductRepository productRepository) {
        this.productNameValidator = productNameValidator;
        this.productRepository = productRepository;
    }

    public void createProduct(ProductCreateRequest request) {

        productNameValidator.validate(request.getName());

        Product product = Product.create(request.getName(), request.getPrice());

        productRepository.save(product);
    }
}
