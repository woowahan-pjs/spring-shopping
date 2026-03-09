package shopping.wishlist.repository;

import org.springframework.data.repository.CrudRepository;
import shopping.wishlist.domain.WishListItem;

public interface WishListItemRepository extends CrudRepository<WishListItem, Long> {

}
