package shopping.product.service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.member.service.MemberService;
import shopping.product.api.ProductCreateRequest;
import shopping.product.api.ProductResponse;
import shopping.product.api.ProductUpdateRequest;
import shopping.product.domain.Product;
import shopping.product.domain.ProductRepository;
import shopping.product.domain.ProductStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[\\p{L}\\p{N}\\s()\\[\\]+&/_-]+$");

    private final MemberService memberService;
    private final ProductRepository productRepository;
    private final PurgomalumClient purgomalumClient;

    public ProductResponse create(Long memberId, ProductCreateRequest request) {
        memberService.requireActiveSeller(memberId);
        validateForWrite(request.name(), request.price(), request.imageUrl());
        String description = normalizeDescription(request.description());
        Product product = Product.create(request.name(), description, request.imageUrl(), request.price(), memberId);
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
        validateOwner(memberId, product);
        validateForWrite(request.name(), request.price(), request.imageUrl());
        String description = normalizeDescription(request.description());
        product.update(request.name(), description, request.imageUrl(), request.price(), memberId);
        return toResponse(product);
    }

    public void delete(Long memberId, Long productId) {
        memberService.requireActiveSeller(memberId);
        Product product = findActive(productId);
        validateOwner(memberId, product);
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

    private void validateForWrite(String name, BigDecimal price, String imageUrl) {
        validateName(name);
        validatePrice(price);
        validateImageUrl(imageUrl);
        validateProfanity(name);
    }

    private void validateName(String name) {
        if (name.length() > 15) {
            throw new ApiException(ErrorCode.PRODUCT_NAME_TOO_LONG);
        }
        if (NAME_PATTERN.matcher(name).matches()) {
            return;
        }
        throw new ApiException(ErrorCode.PRODUCT_NAME_DISALLOWED_SPECIAL_CHARACTERS);
    }

    private void validatePrice(BigDecimal price) {
        if (price.signum() > 0) {
            return;
        }
        throw new ApiException(ErrorCode.PRODUCT_PRICE_INVALID);
    }

    private void validateImageUrl(String imageUrl) {
        URI uri = toUri(imageUrl);
        if (uri.getScheme() != null && uri.getHost() != null) {
            return;
        }
        throw new ApiException(ErrorCode.PRODUCT_IMAGE_URL_NOT_ABSOLUTE);
    }

    private URI toUri(String imageUrl) {
        try {
            return new URI(imageUrl);
        } catch (URISyntaxException exception) {
            throw new ApiException(ErrorCode.PRODUCT_IMAGE_URL_INVALID_FORMAT);
        }
    }

    private void validateProfanity(String name) {
        if (!purgomalumClient.containsProfanity(name)) {
            return;
        }
        throw new ApiException(ErrorCode.PRODUCT_NAME_CONTAINS_PROFANITY);
    }

    private void validateOwner(Long memberId, Product product) {
        if (product.getCreatedMemberId().equals(memberId)) {
            return;
        }
        throw new ApiException(ErrorCode.PRODUCT_OWNER_FORBIDDEN);
    }

    private String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }
        return description;
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
