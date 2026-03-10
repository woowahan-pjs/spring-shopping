package shopping.domain.repository;


import shopping.domain.wishlist.Wishlist;

import java.util.List;

public interface WishlistRepository {
    Wishlist save(Wishlist wishlist);
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
    List<Wishlist> findAllByMemberIdWithProduct(Long memberId);
}

