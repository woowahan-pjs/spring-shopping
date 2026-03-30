package shopping.product.service;

import shopping.product.domain.*;
import shopping.product.dto.ProductResponse;

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
    public ProductResponse execute(UUID id) {
        Product product = productRepository.findByIdAndStatus(id, ProductStatus.CREATED)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        return ProductResponse.from(product);
    }

    @Override
    public List<ProductResponse> execute() {
        return productRepository.findAllByStatus(ProductStatus.CREATED).stream()
                .map(ProductResponse::from).toList();
    }

    @Override
    public List<ProductResponse> execute(List<UUID> ids) {
        return productRepository.findAllByIdInAndStatus(ids, ProductStatus.CREATED).stream()
                .map(ProductResponse::from).toList();
    }
}
