package shopping.wish.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    boolean existsByWishlist_IdAndProductId(Long wishlistId, Long productId);

    List<WishlistItem> findByWishlist_IdOrderByIdAsc(Long wishlistId);

    Optional<WishlistItem> findByIdAndWishlist_Id(Long id, Long wishlistId);
}
