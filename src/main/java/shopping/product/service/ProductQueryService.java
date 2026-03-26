package shopping.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {
    private final ProductRepository productRepository;

    public Product getProduct(Long id) {
        return findProduct(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    public Page<Product> findProductWithPage(Pageable pageable) {
        return productRepository.findAllByDeletedFalse(pageable);
    }

    public Optional<Product> findProduct(Long id) {
        return productRepository.findByIdAndDeletedFalse(id);
    }

    public Map<Long, Product> findProductMap(List<Long> ids) {
        return productRepository.findAllByIdInAndDeletedFalse(ids)
                                .stream()
                                .collect(Collectors.toMap(
                                        Product::getId,
                                        product -> product
                                ));
    }
}
