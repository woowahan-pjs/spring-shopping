package shopping.wishlist.domain;

public interface WishListRepository {
    WishList save(final long productId, final long customerId);
}
