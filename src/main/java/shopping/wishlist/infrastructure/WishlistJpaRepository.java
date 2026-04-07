package shopping.wishlist.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishlistJpaRepository extends JpaRepository<WishlistEntity, Long> {

    Page<WishlistEntity> findAllByMemberId(Long memberId, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE WishlistEntity w SET w.deletedAt = CURRENT_TIMESTAMP WHERE w.id = :id")
    void deleteById(@Param("id") Long id);

    boolean existsByMemberIdAndProductId(long memberId, long productId);

    boolean existsByMemberIdAndId(Long memberId, Long id);
}
