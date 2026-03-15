package shopping.wishlist.service;

import org.springframework.stereotype.Service;
import shopping.product.domain.ProductRepository;
import shopping.wishlist.domain.Wishlist;
import shopping.wishlist.domain.WishlistRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    public void addWishlist(Wishlist wishlist) {
        if (productRepository.findById(wishlist.getProductId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }

        if (wishlistRepository.existsByMemberIdAndProductId(wishlist.getMemberId(), wishlist.getProductId())) {
            throw new IllegalArgumentException("이미 추가된 상품입니다.");
        }

        wishlistRepository.save(wishlist);
    }

    public List<Wishlist> findWishlistItems(long memberId) {
        return wishlistRepository.findAllByMemberId(memberId);
    }

    public void deleteWishlistItem(Long memberId, Long id) {
        if (!wishlistRepository.existsByMemberIdAndId(memberId, id)) {
            throw new NoSuchElementException("존재하지 않은 위시리스트입니다.");
        }
        wishlistRepository.deleteById(id);
    }
}
