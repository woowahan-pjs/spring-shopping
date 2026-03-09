package shopping.product.service;

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
        ProductName name = new ProductName(request.name());
        ProductPrice price = new ProductPrice(request.price());
        ProductImageUrl imageUrl = new ProductImageUrl(request.imageUrl());
        validateSlang(name);
        Product product = Product.create(name, request.description(), imageUrl, price, memberId);
        return toResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductResponse get(Long productId) {
        Product product = findActive(productId);
        return toResponse(product);
    }

    public ProductResponse update(Long memberId, Long productId, ProductUpdateRequest request) {
        memberService.requireActiveSeller(memberId);
        Product product = findActive(productId);
        ProductName name = new ProductName(request.name());
        ProductPrice price = new ProductPrice(request.price());
        ProductImageUrl imageUrl = new ProductImageUrl(request.imageUrl());
        validateSlang(name);
        product.update(name, request.description(), imageUrl, price, memberId);
        return toResponse(product);
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
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Product findActive(Long productId) {
        return productRepository.findByIdAndStatus(productId, ProductStatus.ACTIVE)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private void validateSlang(ProductName name) {
        if (!slangChecker.containsSlang(name.value())) {
            return;
        }
        throw new ApiException(ErrorCode.PRODUCT_NAME_CONTAINS_SLANG);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getStockQuantity()
        );
    }
}
