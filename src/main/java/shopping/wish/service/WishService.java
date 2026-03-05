package shopping.wish.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.product.domain.Product;
import shopping.product.service.ProductService;
import shopping.wish.api.WishResponse;
import shopping.wish.domain.Wishlist;
import shopping.wish.domain.WishlistItem;
import shopping.wish.domain.WishlistItemRepository;
import shopping.wish.domain.WishlistRepository;
import shopping.wish.domain.WishlistStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class WishService {
    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductService productService;

    public WishResponse add(Long memberId, Long productId, Integer requestedQuantity) {
        Wishlist wishlist = findOrCreateWishlist(memberId);
        validateNoDuplicate(wishlist.getId(), productId);
        Product product = productService.findActive(productId);
        Integer quantity = normalizeQuantity(requestedQuantity);
        WishlistItem item = WishlistItem.create(wishlist, productId, quantity);
        WishlistItem saved = wishlistItemRepository.save(item);
        return toResponse(saved, product);
    }

    public void delete(Long memberId, Long wishId) {
        Wishlist wishlist = findActiveWishlist(memberId);
        WishlistItem item = findWishlistItem(wishlist.getId(), wishId);
        wishlistItemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public List<WishResponse> list(Long memberId) {
        Wishlist wishlist = findWishlistForList(memberId);
        if (wishlist == null) {
            return List.of();
        }
        return wishlistItemRepository.findByWishlist_IdOrderByIdAsc(wishlist.getId())
                .stream()
                .map(this::toResponseOrNull)
                .filter(Objects::nonNull)
                .toList();
    }

    private Wishlist findOrCreateWishlist(Long memberId) {
        return wishlistRepository.findByMemberIdAndStatus(memberId, WishlistStatus.ACTIVE)
                .orElseGet(() -> wishlistRepository.save(Wishlist.create(memberId)));
    }

    private Wishlist findActiveWishlist(Long memberId) {
        return wishlistRepository.findByMemberIdAndStatus(memberId, WishlistStatus.ACTIVE)
                .orElseThrow(() -> new ApiException(ErrorCode.WISHLIST_NOT_FOUND));
    }

    private Wishlist findWishlistForList(Long memberId) {
        return wishlistRepository.findByMemberIdAndStatus(memberId, WishlistStatus.ACTIVE)
                .orElse(null);
    }

    private WishlistItem findWishlistItem(Long wishlistId, Long wishId) {
        return wishlistItemRepository.findByIdAndWishlist_Id(wishId, wishlistId)
                .orElseThrow(() -> new ApiException(ErrorCode.WISH_ITEM_NOT_FOUND));
    }

    private void validateNoDuplicate(Long wishlistId, Long productId) {
        boolean exists = wishlistItemRepository.existsByWishlist_IdAndProductId(wishlistId, productId);
        if (!exists) {
            return;
        }
        throw new ApiException(ErrorCode.WISH_ALREADY_EXISTS);
    }

    private Integer normalizeQuantity(Integer requestedQuantity) {
        if (requestedQuantity == null) {
            return 1;
        }
        if (requestedQuantity > 0) {
            return requestedQuantity;
        }
        throw new ApiException(ErrorCode.WISH_QUANTITY_INVALID);
    }

    private WishResponse toResponse(WishlistItem item) {
        Product product = productService.findActive(item.getProductId());
        return toResponse(item, product);
    }

    private WishResponse toResponseOrNull(WishlistItem item) {
        try {
            return toResponse(item);
        } catch (ApiException exception) {
            if (exception.getErrorCode() == ErrorCode.PRODUCT_NOT_FOUND) {
                return null;
            }
            throw exception;
        }
    }

    private WishResponse toResponse(WishlistItem item, Product product) {
        return new WishResponse(
                item.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                item.getQuantity(),
                item.getAddedAt()
        );
    }
}
