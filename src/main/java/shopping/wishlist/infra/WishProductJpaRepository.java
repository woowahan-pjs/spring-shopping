package shopping.wishlist.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.wishlist.domain.WishProduct;

public interface WishProductJpaRepository extends JpaRepository<WishProduct, Long> {
}
