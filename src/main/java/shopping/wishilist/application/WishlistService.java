package shopping.wishilist.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.product.application.ProductProvider;
import shopping.product.domain.Product;
import shopping.wishilist.WishlistRepository;
import shopping.wishilist.application.dto.WishlistRequest;
import shopping.wishilist.application.dto.WishlistResponse;
import shopping.wishilist.domain.Wishlist;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductProvider productProvider;

    public WishlistService(final WishlistRepository wishlistRepository, final ProductProvider productProvider) {
        this.wishlistRepository = wishlistRepository;
        this.productProvider = productProvider;
    }

    @Transactional
    public WishlistResponse addProduct(final Long memberId, final WishlistRequest request) {
        final Wishlist wishlist = findOrCreateWishlistByMember(memberId);
        final Product product = productProvider.findProductById(request.getProductId());

        wishlist.add(product);

        return WishlistResponse.from(wishlist);
    }

    @Transactional
    public WishlistResponse findOrCreateByMember(final Long memberId) {
        final Wishlist wishlist = findOrCreateWishlistByMember(memberId);

        return WishlistResponse.from(wishlist);
    }

    @Transactional
    public void deleteProduct(final Long memberId, final Long productId) {
        final Wishlist wishlist = findOrCreateWishlistByMember(memberId);
        final Product product = productProvider.findProductById(productId);

        wishlist.remove(product);
    }

    private Wishlist findOrCreateWishlistByMember(final Long memberId) {
        final Wishlist wishlist = wishlistRepository.findByMemberId(memberId)
                .orElseGet(() -> new Wishlist(memberId));

        wishlistRepository.save(wishlist);
        return wishlist;
    }
}
