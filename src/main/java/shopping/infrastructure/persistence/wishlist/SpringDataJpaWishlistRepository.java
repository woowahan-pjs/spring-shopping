package shopping.infrastructure.persistence.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shopping.domain.wishlist.Wishlist;

import java.util.List;

public interface SpringDataJpaWishlistRepository extends JpaRepository<Wishlist, Long> {
    boolean existsByMemberIdAndProductId(Long memberId, Long productId);

    @Query("select w from Wishlist w join fetch w.product where w.member.id = :memberId")
    List<Wishlist> findAllByMemberIdWithProduct(Long memberId);
}
