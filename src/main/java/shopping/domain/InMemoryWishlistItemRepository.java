package shopping.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryWishlistItemRepository implements WishlistItemRepository {
    private final Map<Long, WishlistItem> wisilistItemMap = new HashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    @Override
    public WishlistItem save(WishlistItem wishlistItem) {
        long id = idSequence.getAndIncrement();
        wishlistItem.assignId(id);
        wisilistItemMap.put(id, wishlistItem);
        return wishlistItem;
    }

    @Override
    public List<WishlistItem> findAllByMemberId(Long memberId) {
        return wisilistItemMap.values().stream()
                .filter(w -> w.getMemberId().equals(memberId))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        wisilistItemMap.remove(id);
    }

    @Override
    public boolean existsByMemberIdAndProductId(long memberId, long productId) {
        return wisilistItemMap.values().stream()
                .anyMatch(w -> w.getMemberId().equals(memberId) && w.getProductId().equals(productId));
    }
}
