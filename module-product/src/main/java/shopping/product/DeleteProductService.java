package shopping.product;

import java.util.NoSuchElementException;

public class DeleteProductService implements DeleteProduct {

    private final ProductRepository productRepository;

    public DeleteProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void execute(Long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("상품이 존재하지 않습니다.");
        }
        productRepository.deleteById(id);
    }
}
