package shopping.application.wishlist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shopping.domain.member.Member;
import shopping.domain.product.Product;
import shopping.domain.repository.MemberRepository;
import shopping.domain.repository.ProductRepository;
import shopping.domain.repository.WishlistRepository;
import shopping.domain.wishlist.exception.DuplicateWishlistException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class WishlistServiceTest {
    private final WishlistService wishlistService;
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WishlistServiceTest(WishlistService wishlistService, WishlistRepository wishlistRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishlistService = wishlistService;
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    @Test
    @DisplayName("위시리스트에 상품을 정상적으로 추가한다.")
    void addWishlist_success() {
        Member member = memberRepository.save(Member.create("wish@test.com", "pass123!"));
        Product product = productRepository.save(Product.create("맥북", BigDecimal.valueOf(2000000), "test/image_1.jpg"));

        Long wishlistId = wishlistService.addWishlist(member.getId(), product.getId());

        assertThat(wishlistId).isNotNull();
        assertThat(wishlistRepository.existsByMemberIdAndProductId(member.getId(), product.getId())).isTrue();
    }

    @Test
    @DisplayName("이미 위시리스트에 있는 상품을 추가하면 DuplicateWishlistException이 발생한다.")
    void addWishlist_duplicate_throws_exception() {
        // given
        Member member = memberRepository.save(Member.create("duplicate@test.com", "pass123!"));
        Product product = productRepository.save(Product.create("아이폰", BigDecimal.valueOf(1000000), "test/image_1.jpg"));
        wishlistService.addWishlist(member.getId(), product.getId()); // 첫 번째 추가

        // when & then
        assertThatThrownBy(() -> wishlistService.addWishlist(member.getId(), product.getId()))
                .isInstanceOf(DuplicateWishlistException.class);
    }
}