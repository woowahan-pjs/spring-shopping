package shopping.wishlist.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishlistRepository {
    Wishlist save(Wishlist wishlist);
    Page<Wishlist> findAllByMemberId(Long memberId, Pageable pageable);
    void deleteById(Long id);
    boolean existsByMemberIdAndProductId(long memberId, long productId);
    boolean existsByMemberIdAndId(Long memberId, Long id);
}
