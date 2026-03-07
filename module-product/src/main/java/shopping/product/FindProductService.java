package shopping.product;

import java.util.List;
import java.util.NoSuchElementException;

public class FindProductService implements FindProduct {

    private final ProductRepository productRepository;

    public FindProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
    }

    @Override
    public List<Product> execute() {
        return productRepository.findAll();
    }
}
