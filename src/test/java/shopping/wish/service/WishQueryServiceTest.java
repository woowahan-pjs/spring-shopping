package shopping.wish.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.client.ProfanityClient;
import shopping.product.domain.Product;
import shopping.product.repository.ProductRepository;
import shopping.product.service.FakeProfanityClient;
import shopping.product.service.ProductCommandService;
import shopping.product.service.dto.ProductOutput;
import shopping.product.service.dto.ProductRegisterInput;
import shopping.wish.domain.Wish;
import shopping.wish.repository.WishRepository;
import shopping.wish.service.dto.WishOutput;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class WishQueryServiceTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public ProfanityClient fakeProfanityClient() {
            return new FakeProfanityClient();
        }
    }

    @Autowired
    private WishQueryService wishQueryService;

    @Autowired
    private ProductCommandService productCommandService;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FakeProfanityClient profanityClient;

    @Test
    @DisplayName("회원의 활성 위시리스트 목록을 페이징으로 조회한다")
    void test01() {
        // arrange
        Product firstProduct = productRepository.save(
                Product.builder().name("상품1").price(10000L).imageUrl("https://example.com/1.jpg").build()
        );
        Product deletedProduct = productRepository.save(
                Product.builder().name("삭제상품").price(20000L).imageUrl("https://example.com/deleted.jpg").build()
        );
        Product secondProduct = productRepository.save(
                Product.builder().name("상품2").price(30000L).imageUrl("https://example.com/2.jpg").build()
        );
        wishRepository.save(Wish.builder().memberId(1L).productId(firstProduct.getId()).build());
        wishRepository.save(Wish.builder().memberId(1L).productId(secondProduct.getId()).build());
        Wish deletedWish = wishRepository.save(Wish.builder().memberId(1L).productId(deletedProduct.getId()).build());
        deletedWish.delete();
        wishRepository.save(Wish.builder().memberId(2L).productId(firstProduct.getId()).build());

        // act
        Page<WishOutput> result = wishQueryService.findWishWithPage(
                1L,
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))
        );

        // assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().productId()).isEqualTo(secondProduct.getId());
        assertThat(result.getContent().getFirst().productName()).isEqualTo("상품2");
        assertThat(result.getContent().getFirst().price()).isEqualTo(30000L);
        assertThat(result.getContent().getFirst().imageUrl()).isEqualTo("https://example.com/2.jpg");
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("위시리스트가 없는 회원은 빈 목록을 반환한다")
    void test02() {
        // arrange
        Long memberId = 1L;

        // act
        Page<WishOutput> result = wishQueryService.findWishWithPage(
                memberId,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        );

        // assert
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("삭제된 상품이 포함된 위시리스트도 조회할 수 있다")
    void test03() {
        // arrange
        profanityClient.setProfane(false);
        ProductOutput product = productCommandService.register(
            new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg"));
        wishRepository.save(Wish.builder().memberId(1L).productId(product.id()).build());
        productCommandService.delete(product.id());

        // act
        Page<WishOutput> result = wishQueryService.findWishWithPage(
                1L,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        );

        // assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().productId()).isEqualTo(product.id());
        assertThat(result.getContent().getFirst().productName()).isEqualTo("삭제된 상품입니다.");
        assertThat(result.getContent().getFirst().price()).isNull();
        assertThat(result.getContent().getFirst().imageUrl()).isNull();
    }
}
