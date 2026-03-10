package shopping.infrastructure.persistence.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.domain.wishlist.Wishlist;

public interface SpringDataJpaWishlistRepository extends JpaRepository<Wishlist, Long> {
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}
