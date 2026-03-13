package shopping.wishlist.domain;

import java.util.List;

public interface WishlistRepository {
    Wishlist save(Wishlist wishlist);
    List<Wishlist> findAllByMemberId(Long memberId);
    void deleteById(Long id);
    boolean existsByMemberIdAndProductId(long memberId, long productId);
    boolean existsByMemberIdAndId(Long memberId, Long id);
}
