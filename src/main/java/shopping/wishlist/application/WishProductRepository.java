package shopping.wishlist.application;

import shopping.wishlist.domain.WishProduct;

public interface WishProductRepository {
    void save(WishProduct wishProduct);
}
