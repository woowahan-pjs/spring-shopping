package shopping.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.entity.Product;
import shopping.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        if (isProfanity(product.getName())) {
            throw new IllegalArgumentException("Product name contains profanity");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (isProfanity(productDetails.getName())) {
            throw new IllegalArgumentException("Product name contains profanity");
        }

        product.update(productDetails);
        return product;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private boolean isProfanity(String text) {
        return false;
    }
}
