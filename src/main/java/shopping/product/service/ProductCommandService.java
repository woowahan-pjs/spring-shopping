package shopping.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.client.ProductNameValidator;
import shopping.product.domain.Price;
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
        productNameValidator.validate(input.name());
        Product saved = productRepository.save(Product.builder()
                                                      .name(input.name())
                                                      .price(new Price(input.price()))
                                                      .imageUrl(input.imageUrl())
                                                      .build());
        return ProductOutput.from(saved);
    }

    public ProductOutput update(Long id, ProductRegisterInput input) {
        productNameValidator.validate(input.name());
        Product product = getProduct(id);
        product.update(input.name(), new Price(input.price()), input.imageUrl());
        return ProductOutput.from(product);
    }

    public void delete(Long id) {
        getProduct(id).delete();
    }

    private Product getProduct(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }
}
