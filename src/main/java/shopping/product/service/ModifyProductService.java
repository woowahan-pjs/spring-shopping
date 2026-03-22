package shopping.product.service;

import shopping.product.domain.*;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ModifyProductService {

    private final ProductRepository productRepository;

    public ModifyProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void execute(UUID id, ProductName productName, long price, String imageUrl) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        product.update(productName, price, imageUrl);
    }
}
