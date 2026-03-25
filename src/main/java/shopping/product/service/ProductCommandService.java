package shopping.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.domain.Price;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.product.service.dto.ProductRegisterInput;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCommandService {
    private final ProductRepository productRepository;

    public Product register(ProductRegisterInput input) {
        return productRepository.save(Product.create(input.name(), input.price(), input.imageUrl()));
    }

    public Product update(Long id, ProductRegisterInput input) {
        Product product = getProduct(id);
        product.update(input.name(), new Price(input.price()), input.imageUrl());
        return product;
    }

    public void delete(Long id) {
        getProduct(id).delete();
    }

    private Product getProduct(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }
}
