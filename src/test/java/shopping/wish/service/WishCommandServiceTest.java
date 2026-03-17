package shopping.wish.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.client.ProfanityClient;
import shopping.member.domain.Member;
import shopping.member.repository.MemberRepository;
import shopping.product.domain.Price;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.product.service.FakeProfanityClient;
import shopping.wish.domain.Wish;
import shopping.wish.repository.WishRepository;
import shopping.wish.service.dto.WishAddInput;
import shopping.wish.service.dto.WishAddOutput;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class WishCommandServiceTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public ProfanityClient fakeProfanityClient() {
            return new FakeProfanityClient();
        }
    }

    @Autowired
    private WishCommandService wishCommandService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishRepository wishRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(
                Member.builder().email("test@test.com").password("password").build()
        );
    }

    @Test
    @DisplayName("정상 위시리스트 추가 시 WishAddOutput을 반환한다")
    void test01() {
        // given
        Product product = productRepository.save(
                Product.builder().name("상품명").price(new Price(10000L)).imageUrl("https://example.com/image.jpg").build()
        );
        WishAddInput input = new WishAddInput(member.getId(), product.getId());

        // when
        WishAddOutput result = wishCommandService.add(input);

        // then
        assertThat(result.id()).isNotNull();
        assertThat(result.productId()).isEqualTo(product.getId());
        assertThat(result.productName()).isEqualTo("상품명");
    }

    @Test
    @DisplayName("존재하지 않는 상품을 위시리스트에 추가하면 예외가 발생한다")
    void test02() {
        // given
        WishAddInput input = new WishAddInput(member.getId(), 999L);

        // when & then
        assertThatThrownBy(() -> wishCommandService.add(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("정상 위시리스트 삭제 시 삭제 처리된다")
    void test03() {
        // given
        Product product = productRepository.save(
                Product.builder().name("상품명").price(new Price(10000L)).imageUrl("https://example.com/image.jpg").build()
        );
        WishAddOutput added = wishCommandService.add(new WishAddInput(member.getId(), product.getId()));

        // when
        wishCommandService.delete(added.id(), member.getId());

        // then
        Wish wish = wishRepository.findById(added.id()).get();
        assertThat(wish.isDeleted()).isTrue();
        assertThat(wish.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 위시리스트를 삭제하면 예외가 발생한다")
    void test04() {
        // when & then
        assertThatThrownBy(() -> wishCommandService.delete(999L, member.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 위시리스트입니다.");
    }

    @Test
    @DisplayName("이미 위시리스트에 있는 상품을 추가하면 예외가 발생한다")
    void test05() {
        // given
        Product product = productRepository.save(
                Product.builder().name("상품명").price(new Price(10000L)).imageUrl("https://example.com/image.jpg").build()
        );
        wishCommandService.add(new WishAddInput(member.getId(), product.getId()));

        // when & then
        assertThatThrownBy(() -> wishCommandService.add(new WishAddInput(member.getId(), product.getId())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 위시리스트에 있는 상품입니다.");
    }

    @Test
    @DisplayName("타인의 위시리스트를 삭제하면 예외가 발생한다")
    void test06() {
        // given
        Product product = productRepository.save(
                Product.builder().name("상품명").price(new Price(10000L)).imageUrl("https://example.com/image.jpg").build()
        );
        WishAddOutput added = wishCommandService.add(new WishAddInput(member.getId(), product.getId()));

        Member other = memberRepository.save(
                Member.builder().email("other@test.com").password("password").build()
        );

        // when & then
        assertThatThrownBy(() -> wishCommandService.delete(added.id(), other.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 위시리스트입니다.");
    }
}