package shopping.wishlist.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListEntityJpaRepository extends JpaRepository<WishListEntity, Long> {
    Optional<WishListEntity> findByCustomerIdAndProductId(final long customerId, final long productId);
}
