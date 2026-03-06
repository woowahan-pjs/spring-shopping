package shopping.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.client.ProductNameValidator;
import shopping.product.api.command.dto.ProductRegisterRequest;
import shopping.product.api.query.dto.ProductDetailResponse;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCommandService {
    private final ProductRepository productRepository;
    private final ProductNameValidator productNameValidator;
    private final ProductQueryService productQueryService;

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

    public ProductDetailResponse update(Long id, ProductRegisterRequest request) {
        productNameValidator.validate(request.name());
        validatePrice(request.price());
        Product product = productQueryService.getProduct(id);
        product.update(request.name(), request.price(), request.imageUrl());
        return ProductDetailResponse.from(product);
    }

    public void delete(Long id) {
        productRepository.delete(productQueryService.getProduct(id));
    }

    private void validatePrice(Long price) {
        if (price <= 0) {
            throw new IllegalArgumentException("가격은 양수이어야 합니다.");
        }
    }
}