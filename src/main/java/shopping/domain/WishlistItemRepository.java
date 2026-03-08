package shopping.domain;

import java.util.List;

public interface WishlistItemRepository {
    WishlistItem save(WishlistItem wishlistItem);
    List<WishlistItem> findAllByMemberId(Long memberId);
}
