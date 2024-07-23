package shopping.shop.application.query;

import shopping.shop.domain.Shop;

public record ShopRegistrationQuery(long id, long sellerId, String name) {
    public ShopRegistrationQuery(final Shop shop) {
        this(shop.id(), shop.sellerId(), shop.name());
    }
}