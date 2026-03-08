package shopping.service;

import shopping.domain.ProductRepository;
import shopping.domain.WishlistItem;
import shopping.domain.WishlistItemRepository;

public class WishlistItemService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;

    public WishlistItemService(WishlistItemRepository wishlistItemRepository, ProductRepository productRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
    }

    public void addWishlistItem(WishlistItem wishlistItem) {
        if (productRepository.findById(wishlistItem.getProductId()) == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }

        wishlistItemRepository.save(wishlistItem);
    }
}
