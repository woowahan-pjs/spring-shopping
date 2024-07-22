package shopping.wishlist.application;

import org.springframework.stereotype.Service;
import shopping.wishlist.application.command.WishListRegistrationCommand;
import shopping.wishlist.domain.WishList;
import shopping.wishlist.domain.WishListRepository;

@Service
public class WishListService implements WishListRegistrationUseCase {

    private final WishListRepository wishListRepository;

    public WishListService(final WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    @Override
    public WishList register(final WishListRegistrationCommand command) {
        return wishListRepository.save(command.productId(), command.customerId());
    }
}
