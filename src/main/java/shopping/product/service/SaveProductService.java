package shopping.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import shopping.product.domain.*;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SaveProductService {

    private static final Logger log = LoggerFactory.getLogger(SaveProductService.class);

    private final ProductRepository productRepository;

    public SaveProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(ProductName productName, long price, String imageUrl) {
        log.info("[SaveProduct] inside transaction, saving product name='{}'",
                productName.getValue());
        Product product = new Product(productName, price, imageUrl);
        Product saved = productRepository.save(product);
        log.info("[SaveProduct] product saved, id={}", saved.getId());
        return saved;
    }
}
