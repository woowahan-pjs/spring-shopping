package shopping.wishlist.application;

import shopping.wishlist.application.command.WishListRegistrationCommand;
import shopping.wishlist.application.query.WishListRegistrationQuery;

public interface WishListRegistrationUseCase {
    WishListRegistrationQuery register(WishListRegistrationCommand command);
}
