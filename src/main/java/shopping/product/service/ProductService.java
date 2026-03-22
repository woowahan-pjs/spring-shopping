package shopping.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;

import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProfanityValidator profanityValidator;

    public ProductService(ProductRepository productRepository, ProfanityValidator profanityValidator) {
        this.productRepository = productRepository;
        this.profanityValidator = profanityValidator;
    }

    public Product save(Product product) {
        validateProductName(product);

        return productRepository.save(product);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다."));
    }

    public Page<Product> findProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional
    public Product update(Long id, Product product) {
        validateProductName(product);
        Product found = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다."));
        found.update(product);
        return productRepository.save(found);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    private void validateProductName(Product product) {
        if (profanityValidator.containsProfanity(product.getName())) {
            throw new IllegalArgumentException("비속어가 포함되어 있습니다.");
        }
    }
}
