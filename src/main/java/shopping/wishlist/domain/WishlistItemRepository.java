package shopping.wishlist.domain;

import java.util.List;

public interface WishlistItemRepository {
    WishlistItem save(WishlistItem wishlistItem);
    List<WishlistItem> findAllByMemberId(Long memberId);
    void deleteById(Long id);
    boolean existsByMemberIdAndProductId(long memberId, long productId);
    boolean existsByMemberIdAndId(Long memberId, Long id);
}
