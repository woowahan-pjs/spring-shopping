package shopping.wishlist.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.product.domain.Product;

public interface WishListRepository extends JpaRepository<WishList, Long> {
}
