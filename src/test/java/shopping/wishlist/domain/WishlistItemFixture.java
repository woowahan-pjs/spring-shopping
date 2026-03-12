package shopping.wishlist.domain;

public class WishlistItemFixture {
    public static final Long VALID_MEMBER_ID = 1L;

    public static WishlistItem createWishItem(Long id, Long productId) {
        WishlistItem wishlistItem = new WishlistItem(VALID_MEMBER_ID, productId);
        wishlistItem.assignId(id);
        return wishlistItem;
    }
}
