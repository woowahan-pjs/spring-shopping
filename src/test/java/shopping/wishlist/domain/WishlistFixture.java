package shopping.wishlist.domain;

public class WishlistFixture {
    public static final Long VALID_MEMBER_ID = 1L;

    public static Wishlist createWish(Long id, Long productId) {
        Wishlist wishlist = new Wishlist(VALID_MEMBER_ID, productId);
        wishlist.assignId(id);
        return wishlist;
    }
}
