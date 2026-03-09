package shopping.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.repository.ProductRepository;
import shopping.product.service.dto.ProductOutput;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {
    private final ProductRepository productRepository;

    public ProductOutput findById(Long id) {
        return findProduct(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    public List<ProductOutput> findAll() {
        return productRepository.findAllByDeletedFalse().stream()
                                .map(ProductOutput::from)
                                .toList();
    }

    public Optional<ProductOutput> findProduct(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                                .map(ProductOutput::from);
    }
}
