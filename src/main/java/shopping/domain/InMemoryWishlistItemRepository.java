package shopping.domain;

import java.util.HashMap;
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
}
