package shopping.wishlist.service;

import org.springframework.stereotype.Service;
import shopping.product.domain.ProductRepository;
import shopping.wishlist.domain.WishlistItem;
import shopping.wishlist.domain.WishlistItemRepository;

import java.util.List;

@Service
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

        if (wishlistItemRepository.existsByMemberIdAndProductId(wishlistItem.getMemberId(), wishlistItem.getProductId())) {
            throw new IllegalArgumentException("이미 추가된 상품입니다.");
        }

        wishlistItemRepository.save(wishlistItem);
    }

    public List<WishlistItem> findWishlistItems(long memberId) {
        return wishlistItemRepository.findAllByMemberId(memberId);
    }

    public void deleteWishlistItem(Long memberId, Long id) {
        if (!wishlistItemRepository.existsByMemberIdAndId(memberId, id)) {
            throw new IllegalArgumentException("존재하지 않은 위시리스트입니다.");
        }
        wishlistItemRepository.deleteById(id);
    }
}
