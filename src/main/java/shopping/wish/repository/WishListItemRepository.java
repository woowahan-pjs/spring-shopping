package shopping.wish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shopping.wish.domain.WishListItem;

import java.util.List;

public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {

    List<WishListItem> findByWishListId(Long wishListId);
}
