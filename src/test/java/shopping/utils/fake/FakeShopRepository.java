package shopping.utils.fake;

import shopping.shop.domain.Shop;
import shopping.shop.domain.ShopRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakeShopRepository implements ShopRepository {
    private final Map<Long, Shop> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Shop save(final Shop shop) {
        if(Objects.isNull(shop.id())) {
            final var id = idGenerator.incrementAndGet();
            final var newShop = new Shop(
                    id,
                    shop.sellerId(),
                    shop.name()
            );
            storage.put(id, newShop);
            return newShop;
        }
        return storage.put(shop.id(), shop);
    }
}
