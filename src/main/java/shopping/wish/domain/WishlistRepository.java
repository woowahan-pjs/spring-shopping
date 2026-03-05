package shopping.wish.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByMemberIdAndStatus(Long memberId, WishlistStatus status);
}
