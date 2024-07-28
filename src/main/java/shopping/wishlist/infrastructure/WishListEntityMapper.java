package shopping.wishlist.infrastructure;

import shopping.wishlist.domain.WishList;
import shopping.wishlist.infrastructure.persistence.WishListEntity;

public class WishListEntityMapper {
    private WishListEntityMapper() {
    }

    public static WishListEntity domainToEntity(final WishList WishList) {
        return new WishListEntity(WishList.id(), WishList.productId(), WishList.customerId());
    }

    public static WishList entityToDomain(final WishListEntity wishListEntity) {
        return new WishList(wishListEntity.getId(), wishListEntity.getProductId(), wishListEntity.getCustomerId());
    }
}
