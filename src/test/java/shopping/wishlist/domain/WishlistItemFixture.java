package shopping.wishlist.domain;

public class WishlistItemFixture {
    public static final Long VALID_MEMBER_ID = 1L;

    public static Wishlist createWishItem(Long id, Long productId) {
        Wishlist wishlist = new Wishlist(VALID_MEMBER_ID, productId);
        wishlist.assignId(id);
        return wishlist;
    }
}
