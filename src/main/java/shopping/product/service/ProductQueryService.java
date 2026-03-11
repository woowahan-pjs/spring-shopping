package shopping.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.repository.ProductRepository;
import shopping.product.service.dto.ProductOutput;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {
    private final ProductRepository productRepository;

    public ProductOutput getProduct(Long id) {
        return findProduct(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    public Page<ProductOutput> findProductWithPage(Pageable pageable) {
        return productRepository.findAllByDeletedFalse(pageable)
                                .map(ProductOutput::from);
    }

    public Optional<ProductOutput> findProduct(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                                .map(ProductOutput::from);
    }
}
