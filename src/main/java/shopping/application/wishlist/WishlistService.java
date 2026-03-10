package shopping.application.wishlist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.domain.member.Member;
import shopping.domain.member.exception.MemberNotFoundException;
import shopping.domain.product.Product;
import shopping.domain.product.exception.ProductNotFoundException;
import shopping.domain.repository.MemberRepository;
import shopping.domain.repository.ProductRepository;
import shopping.domain.repository.WishlistRepository;
import shopping.domain.wishlist.Wishlist;
import shopping.domain.wishlist.exception.DuplicateWishlistException;
import shopping.dto.WishlistResponse;

import java.util.List;

@Service
@Transactional
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Long addWishlist(Long memberId, Long productId) {
        if (wishlistRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw new DuplicateWishlistException(productId);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Wishlist wishlist = Wishlist.create(member, product);
        return wishlistRepository.save(wishlist).getId();
    }

    @Transactional(readOnly = true)
    public List<WishlistResponse> getWishlist(Long memberId) {
        List<Wishlist> wishlists = wishlistRepository.findAllByMemberIdWithProduct(memberId);

        return wishlists.stream()
                .map(w -> new WishlistResponse(
                        w.getId(),
                        w.getProduct().getId(),
                        w.getProduct().getName(),
                        w.getProduct().getPrice()
                ))
                .toList();
    }

    public void removeWishlist(Long memberId, Long productId) {
        if (!wishlistRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw new ProductNotFoundException("위시리스트에 해당 상품이 존재하지 않습니다.");
        }

        wishlistRepository.deleteByMemberIdAndProductId(memberId, productId);
    }
}
