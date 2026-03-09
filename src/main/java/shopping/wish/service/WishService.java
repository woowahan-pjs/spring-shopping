package shopping.wish.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.product.port.out.ProductSnapshot;
import shopping.product.port.out.ProductSnapshotProvider;
import shopping.wish.adapter.in.api.WishResponse;
import shopping.wish.domain.Wishlist;
import shopping.wish.domain.WishlistItem;
import shopping.wish.domain.WishQuantity;
import shopping.wish.domain.WishlistItemRepository;
import shopping.wish.domain.WishlistRepository;
import shopping.wish.domain.WishlistStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class WishService {
    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductSnapshotProvider productSnapshotProvider;

    public WishResponse add(Long memberId, Long productId, Integer requestedQuantity) {
        Wishlist wishlist = findOrCreateWishlist(memberId);
        validateNoDuplicate(wishlist.getId(), productId);
        ProductSnapshot product = productSnapshotProvider.getActiveProduct(productId);
        WishQuantity quantity = WishQuantity.from(requestedQuantity);
        WishlistItem item = WishlistItem.create(wishlist, productId, quantity);
        WishlistItem saved = wishlistItemRepository.save(item);
        return WishResponse.from(saved, product);
    }

    public void delete(Long memberId, Long wishId) {
        Wishlist wishlist = findActiveWishlist(memberId);
        WishlistItem item = findWishlistItem(wishlist.getId(), wishId);
        wishlistItemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public List<WishResponse> list(Long memberId) {
        return findWishlistForList(memberId)
                .map(this::listResponses)
                .orElseGet(List::of);
    }

    private Wishlist findOrCreateWishlist(Long memberId) {
        return wishlistRepository.findByMemberIdAndStatus(memberId, WishlistStatus.ACTIVE)
                .orElseGet(() -> wishlistRepository.save(Wishlist.create(memberId)));
    }

    private Wishlist findActiveWishlist(Long memberId) {
        return wishlistRepository.findByMemberIdAndStatus(memberId, WishlistStatus.ACTIVE)
                .orElseThrow(() -> new ApiException(ErrorCode.WISHLIST_NOT_FOUND));
    }

    private Optional<Wishlist> findWishlistForList(Long memberId) {
        return wishlistRepository.findByMemberIdAndStatus(memberId, WishlistStatus.ACTIVE);
    }

    private List<WishResponse> listResponses(Wishlist wishlist) {
        return wishlistItemRepository.findByWishlist_IdOrderByIdAsc(wishlist.getId())
                .stream()
                .map(this::toResponseIfProductIsActive)
                .flatMap(Optional::stream)
                .toList();
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

    private Optional<WishResponse> toResponseIfProductIsActive(WishlistItem item) {
        return productSnapshotProvider.findActiveProduct(item.getProductId())
                .map(product -> WishResponse.from(item, product));
    }
}
