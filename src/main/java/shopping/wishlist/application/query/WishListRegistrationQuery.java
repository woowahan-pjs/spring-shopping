package shopping.wishlist.application.query;

import shopping.wishlist.domain.WishList;

public record WishListRegistrationQuery(Long id, long productId, long customerId) {
    public WishListRegistrationQuery(final WishList wishList) {
        this(wishList.id(), wishList.productId(), wishList.customerId());
    }
}
