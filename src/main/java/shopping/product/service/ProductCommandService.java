package shopping.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.client.ProductNameValidator;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.product.service.dto.ProductOutput;
import shopping.product.service.dto.ProductRegisterInput;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCommandService {
    private final ProductRepository productRepository;
    private final ProductNameValidator productNameValidator;

    public ProductOutput register(ProductRegisterInput input) {
        validate(input);
        Product saved = productRepository.save(Product.builder()
                                                      .name(input.name())
                                                      .price(input.price())
                                                      .imageUrl(input.imageUrl())
                                                      .build());
        return ProductOutput.from(saved);
    }

    public ProductOutput update(Long id, ProductRegisterInput input) {
        validate(input);
        Product product = getProduct(id);
        product.update(input.name(), input.price(), input.imageUrl());
        return ProductOutput.from(product);
    }

    public void delete(Long id) {
        getProduct(id).delete();
    }

    private void validate(ProductRegisterInput input) {
        productNameValidator.validate(input.name());
        validatePrice(input.price());
    }

    private void validatePrice(Long price) {
        if (price <= 0) {
            throw new IllegalArgumentException("가격은 양수이어야 합니다.");
        }
    }

    private Product getProduct(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }
}
