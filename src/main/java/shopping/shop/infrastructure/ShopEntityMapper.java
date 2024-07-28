package shopping.shop.infrastructure;

import shopping.shop.domain.Shop;
import shopping.shop.infrastructure.pesistence.ShopEntity;

public class ShopEntityMapper {
    private ShopEntityMapper() {
    }

    public static ShopEntity domainToEntity(final Shop shop) {
        return new ShopEntity(null, shop.sellerId(), shop.name());
    }

    public static Shop entityToDomain(final ShopEntity shopEntity) {
        return new Shop(shopEntity.getId(), shopEntity.getSellerId(), shopEntity.getName());
    }
}
