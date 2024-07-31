package shopping.wishilist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shopping.wishilist.domain.Wishlist;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    @Query("select wishlist from Wishlist wishlist left join fetch wishlist.wishlistItems.wishlistItems wishitems join fetch wishitems.product where wishlist.memberId = :memberId")
    Optional<Wishlist> findByMemberId(@Param("memberId") Long memberId);
}
