package shopping.wishlist.domain;

import org.springframework.test.util.ReflectionTestUtils;
import shopping.product.domain.Product;

public class WishListItemFixture {

    public static WishListItem fixture(final Long id, final Long wishListId, final Product product) {
        WishListItem wishListItem = new WishListItem();

        ReflectionTestUtils.setField(wishListItem, "id", id);
        ReflectionTestUtils.setField(wishListItem, "wishListId", wishListId);
        ReflectionTestUtils.setField(wishListItem, "product", product);
        ReflectionTestUtils.setField(wishListItem, "isUse", true);

        return wishListItem;
    }
}
