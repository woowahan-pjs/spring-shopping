package shopping.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.client.ProductNameValidator;
import shopping.product.api.dto.ProductDetailResponse;
import shopping.product.api.dto.ProductRegisterRequest;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductNameValidator productNameValidator;

    @Transactional
    public ProductDetailResponse register(ProductRegisterRequest request) {
        productNameValidator.validate(request.name());
        validatePrice(request.price());
        Product saved = productRepository.save(Product.builder()
                                                      .name(request.name())
                                                      .price(request.price())
                                                      .imageUrl(request.imageUrl())
                                                      .build());
        return ProductDetailResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse findById(Long id) {
        return ProductDetailResponse.from(getProduct(id));
    }

    @Transactional(readOnly = true)
    public List<ProductDetailResponse> findAll() {
        return productRepository.findAll().stream()
                                .map(ProductDetailResponse::from)
                                .toList();
    }

    @Transactional
    public ProductDetailResponse update(Long id, ProductRegisterRequest request) {
        productNameValidator.validate(request.name());
        validatePrice(request.price());
        Product product = getProduct(id);
        product.update(request.name(), request.price(), request.imageUrl());
        return ProductDetailResponse.from(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.delete(getProduct(id));
    }

    private void validatePrice(Long price) {
        if (price <= 0) {
            throw new IllegalArgumentException("가격은 양수이어야 합니다.");
        }
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }
}
