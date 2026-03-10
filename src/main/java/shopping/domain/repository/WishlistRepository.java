package shopping.domain.repository;


import shopping.domain.wishlist.Wishlist;

public interface WishlistRepository {
    Wishlist save(Wishlist wishlist);
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}

