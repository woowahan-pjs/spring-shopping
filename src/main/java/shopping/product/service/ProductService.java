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
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final int MAX_NAME_LENGTH = 15;
    private static final Pattern ALLOWED_NAME_PATTERN =
        Pattern.compile("^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$");

    private final ProductRepository productRepository;
    private final ProductNameValidator productNameValidator;

    @Transactional
    public ProductDetailResponse register(ProductRegisterRequest request) {
        validateName(request.name());
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
        validateName(request.name());
        validatePrice(request.price());
        Product product = getProduct(id);
        product.update(request.name(), request.price(), request.imageUrl());
        return ProductDetailResponse.from(product);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.delete(getProduct(id));
    }

    private void validateName(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("상품명은 15자 이하이어야 합니다.");
        }
        if (!ALLOWED_NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("상품명에 허용되지 않은 특수문자가 포함되어 있습니다.");
        }
        if (productNameValidator.containsProfanity(name)) {
            throw new IllegalArgumentException("상품명에 비속어가 포함되어 있습니다.");
        }
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
