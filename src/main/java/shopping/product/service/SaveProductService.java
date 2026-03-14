package shopping.product.service;

import shopping.product.domain.*;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SaveProductService {

    private final ProductRepository productRepository;

    public SaveProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(ProductName productName, long price, String imageUrl) {
        Product product = new Product(productName, price, imageUrl);
        return productRepository.save(product);
    }
}
