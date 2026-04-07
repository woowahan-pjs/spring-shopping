package shopping.wishlist.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryWishlistRepository implements WishlistRepository {
    private final Map<Long, Wishlist> wisilistItemMap = new HashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    @Override
    public Wishlist save(Wishlist wishlist) {
        long id = idSequence.getAndIncrement();
        wishlist.assignId(id);
        wisilistItemMap.put(id, wishlist);
        return wishlist;
    }

    @Override
    public Page<Wishlist> findAllByMemberId(Long memberId, Pageable pageable) {
        List<Wishlist> list = wisilistItemMap.values().stream()
                .filter(w -> w.getMemberId().equals(memberId))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
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

    @Override
    public boolean existsByMemberIdAndId(Long memberId, Long id) {
        return wisilistItemMap.values().stream()
                .anyMatch(w -> w.getMemberId().equals(memberId) && w.getId().equals(id));
    }
}
