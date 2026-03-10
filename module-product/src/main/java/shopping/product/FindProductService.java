package shopping.product;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class FindProductService implements FindProduct {

    private final ProductRepository productRepository;

    public FindProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(UUID id) {
        return productRepository.findByIdAndStatus(id, ProductStatus.CREATED)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
    }

    @Override
    public List<Product> execute() {
        return productRepository.findAllByStatus(ProductStatus.CREATED);
    }
}
