package shopping.wishlist.application;

import shopping.wishlist.application.command.WishListRegistrationCommand;
import shopping.wishlist.domain.WishList;

public interface WishListRegistrationUseCase {
    WishList register(WishListRegistrationCommand command);
}
