package shopping.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shopping.infra.exception.ShoppingBusinessException;
import shopping.product.domain.Product;
import shopping.product.dto.ProductResponse;
import shopping.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 주어진 상품 ID를 이용하여 활성화된 상품 정보를 조회하고, ProductResponse로 반환합니다.
     *
     * @param productId 조회할 상품의 고유 ID입니다.
     * @return 조회된 상품 정보를 담고 있는 ProductResponse 객체를 반환합니다. 활성화된 상품이 존재하지 않을 경우 예외가 발생합니다.
     */
    @Transactional(readOnly = true)
    public ProductResponse getProduct(final Long productId) {
        final Product product =
                productRepository
                        .findProductByIdAndIsUse(productId, true)
                        .orElseThrow(() -> new ShoppingBusinessException("상품이 존재하지 않습니다."));

        return ProductResponse.from(
                product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }
}
