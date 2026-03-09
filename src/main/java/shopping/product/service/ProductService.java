package shopping.product.service;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.member.service.MemberService;
import shopping.product.adapter.in.api.ProductCreateRequest;
import shopping.product.adapter.in.api.ProductResponse;
import shopping.product.adapter.in.api.ProductUpdateRequest;
import shopping.product.domain.Product;
import shopping.product.domain.ProductDetails;
import shopping.product.domain.ProductImageUrl;
import shopping.product.domain.ProductName;
import shopping.product.domain.ProductPrice;
import shopping.product.domain.ProductRepository;
import shopping.product.domain.ProductStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final MemberService memberService;
    private final ProductRepository productRepository;
    private final SlangChecker slangChecker;

    public ProductResponse create(Long memberId, ProductCreateRequest request) {
        memberService.requireActiveSeller(memberId);
        ProductDetails details = toProductDetails(
                request.name(),
                request.description(),
                request.price(),
                request.imageUrl()
        );
        Product product = Product.create(details, memberId);
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductResponse get(Long productId) {
        return ProductResponse.from(findActive(productId));
    }

    public ProductResponse update(Long memberId, Long productId, ProductUpdateRequest request) {
        memberService.requireActiveSeller(memberId);
        Product product = findActive(productId);
        ProductDetails details = toProductDetails(
                request.name(),
                request.description(),
                request.price(),
                request.imageUrl()
        );
        product.update(details, memberId);
        return ProductResponse.from(product);
    }

    public void delete(Long memberId, Long productId) {
        memberService.requireActiveSeller(memberId);
        Product product = findActive(productId);
        product.delete(memberId);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findByStatusOrderByIdAsc(ProductStatus.ACTIVE)
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    private Product findActive(Long productId) {
        return productRepository.findByIdAndStatus(productId, ProductStatus.ACTIVE)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private ProductDetails toProductDetails(
            String name,
            String description,
            BigDecimal price,
            String imageUrl
    ) {
        ProductDetails details = new ProductDetails(
                new ProductName(name),
                description,
                new ProductImageUrl(imageUrl),
                new ProductPrice(price)
        );
        if (!slangChecker.containsSlang(details.name().value())) {
            return details;
        }
        throw new ApiException(ErrorCode.PRODUCT_NAME_CONTAINS_SLANG);
    }
}
