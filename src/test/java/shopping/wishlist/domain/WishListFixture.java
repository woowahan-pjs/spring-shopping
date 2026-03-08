package shopping.wishlist.domain;

import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

public class WishListFixture {

    public static WishList fixture(final Long id, final Long userId, List<WishListItem> items) {
        WishList wishList = new WishList();

        ReflectionTestUtils.setField(wishList, "id", id);
        ReflectionTestUtils.setField(wishList, "userId", userId);
        ReflectionTestUtils.setField(wishList, "items", items);

        return wishList;
    }
}
