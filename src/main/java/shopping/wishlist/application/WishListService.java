package shopping.wishlist.application;

import org.springframework.stereotype.Service;
import shopping.wishlist.application.command.WishListRegistrationCommand;
import shopping.wishlist.application.query.WishListRegistrationQuery;
import shopping.wishlist.domain.WishList;
import shopping.wishlist.domain.WishListRepository;

@Service
public class WishListService implements WishListRegistrationUseCase {

    private final WishListRepository wishListRepository;

    public WishListService(final WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    @Override
    public WishListRegistrationQuery register(final WishListRegistrationCommand command) {
        final WishList wishList = wishListRepository.save(init(command));
        return new WishListRegistrationQuery(wishList);
    }

    private WishList init(final WishListRegistrationCommand command) {
        return new WishList(null, command.productId(), command.customerId());
    }
}
