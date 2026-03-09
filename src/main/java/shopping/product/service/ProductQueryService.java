package shopping.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.product.service.dto.ProductOutput;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {
    private final ProductRepository productRepository;

    public ProductOutput findById(Long id) {
        return ProductOutput.from(getActiveProduct(id));
    }

    public List<ProductOutput> findAll() {
        return productRepository.findAllByDeletedFalse().stream()
                                .map(ProductOutput::from)
                                .toList();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    public Product getActiveProduct(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }
}
