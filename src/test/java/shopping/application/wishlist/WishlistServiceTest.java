package shopping.application.wishlist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shopping.domain.member.Member;
import shopping.domain.product.Product;
import shopping.domain.product.exception.ProductNotFoundException;
import shopping.domain.repository.MemberRepository;
import shopping.domain.repository.ProductRepository;
import shopping.domain.repository.WishlistRepository;
import shopping.domain.wishlist.exception.DuplicateWishlistException;
import shopping.dto.WishlistResponse;

import java.math.BigDecimal;
import java.util.List;

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

    @Test
    @DisplayName("사용자의 위시리스트 목록을 조회하면 상품 정보가 반환되어야 한다.")
    void getWishlist_success() {
        Member member = memberRepository.save(Member.create("list@test.com", "pass123!"));
        Product product1 = productRepository.save(Product.create("맥북", BigDecimal.valueOf(2000000), "test/image_1.jpg"));
        Product product2 = productRepository.save(Product.create("아이폰", BigDecimal.valueOf(1000000), "test/image_1.jpg"));

        wishlistService.addWishlist(member.getId(), product1.getId());
        wishlistService.addWishlist(member.getId(), product2.getId());

        // when
        List<WishlistResponse> responses = wishlistService.getWishlist(member.getId());

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting("productName").containsExactlyInAnyOrder("맥북", "아이폰");
        assertThat(responses).extracting("price")
                .containsExactlyInAnyOrder(new BigDecimal("2000000"), new BigDecimal("1000000"));
    }

    @Test
    @DisplayName("위시리스트에서 상품을 삭제하면 목록에서 사라져야 한다.")
    void removeWishlist_success() {
        Member member = memberRepository.save(Member.create("remove@test.com", "pass123!"));
        Product product = productRepository.save(Product.create("맥북", BigDecimal.valueOf(2000000), "test/image_1.jpg"));
        wishlistService.addWishlist(member.getId(), product.getId());

        wishlistService.removeWishlist(member.getId(), product.getId());

        boolean exists = wishlistRepository.existsByMemberIdAndProductId(member.getId(), product.getId());
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 위시리스트 항목을 삭제하려고 하면 예외가 발생한다.")
    void removeWishlist_fail_not_found() {
        Long memberId = 1L;
        Long productId = 999L;

        assertThatThrownBy(() -> wishlistService.removeWishlist(memberId, productId))
                .isInstanceOf(ProductNotFoundException.class);
    }
}