package shopping.wishlist.application;

import shopping.wishlist.application.command.WishListRegistrationCommand;
import shopping.wishlist.application.query.WishListRegistrationQuery;
import shopping.wishlist.domain.WishList;

public interface WishListRegistrationUseCase {
    WishListRegistrationQuery register(WishListRegistrationCommand command);
}
