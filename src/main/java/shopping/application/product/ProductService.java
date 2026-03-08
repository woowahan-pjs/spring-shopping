package shopping.application.product;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import shopping.domain.product.Price;
import shopping.domain.product.Product;
import shopping.domain.product.ProductName;
import shopping.domain.product.ProfanityChecker;
import shopping.domain.product.exception.ProductNotFoundException;
import shopping.domain.product.exception.ProfanityNameException;
import shopping.domain.repository.ProductRepository;
import shopping.dto.ProductRequest;

@Service
public class ProductService {
    private final ProfanityChecker profanityChecker;
    private final ProductRepository productRepository;

    public ProductService(ProfanityChecker profanityChecker, ProductRepository productRepository) {
        this.profanityChecker = profanityChecker;
        this.productRepository = productRepository;
    }

    @Transactional
    public Long createProduct(ProductRequest request) {
        if(profanityChecker.containsProfanity(request.getName())) {
            throw new ProfanityNameException(request.getName());
        }

        Product product = Product.create(request.getName(), request.getPrice(), request.getImageUrl());

        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }

    @Transactional
    public void updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        if(profanityChecker.containsProfanity(request.getName())) {
            throw new ProfanityNameException(request.getName());
        }

        ProductName newName = new ProductName(request.getName());
        Price newPrice = new Price(request.getPrice());

        product.update(newName, newPrice, request.getImageUrl());
    }
}
