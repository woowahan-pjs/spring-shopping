package shopping.utils.fake;

import shopping.wishlist.domain.WishList;
import shopping.wishlist.domain.WishListRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakeWishListRepository implements WishListRepository {
    private final Map<Long, WishList> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public WishList save(final WishList wishListRegistration) {
        if (Objects.isNull(wishListRegistration.id())) {
            final var id = idGenerator.incrementAndGet();
            final var wishList = new WishList(
                    id,
                    wishListRegistration.productId(),
                    wishListRegistration.customerId()
            );
            storage.put(id, wishList);
            return wishList;
        }
        return storage.put(wishListRegistration.id(), wishListRegistration);
    }
}
