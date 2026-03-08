package shopping.product.service;

import shopping.product.domain.Product;
import shopping.product.dto.ProductCreateRequest;
import shopping.product.dto.ProductResponse;
import shopping.product.dto.ProductUpdateRequest;
import shopping.product.repository.ProductRepository;
import shopping.product.validator.ProductNameValidator;

import java.util.List;
import java.util.NoSuchElementException;

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

    public ProductResponse getProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));

        return ProductResponse.from(product);
    }

    public void updateProduct(Long productId, ProductUpdateRequest request) {

        productNameValidator.validate(request.getName());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));

        product.update(request.getName(), request.getPrice(), request.getImageUrl());
    }

    public void deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));

        productRepository.delete(product);
    }

    public List<ProductResponse> getProduct() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }
}
