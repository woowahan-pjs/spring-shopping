package shopping.wish.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
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

import java.util.List;

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
    @DisplayName("회원의 활성 위시리스트 목록을 조회한다")
    void test01() {
        // arrange
        Product product = productRepository.save(
                Product.builder().name("상품명").price(10000L).imageUrl("https://example.com/image.jpg").build()
        );
        Product deletedProduct = productRepository.save(
                Product.builder().name("삭제상품").price(20000L).imageUrl("https://example.com/deleted.jpg").build()
        );
        wishRepository.save(Wish.builder().memberId(1L).productId(product.getId()).build());
        Wish deletedWish = wishRepository.save(Wish.builder().memberId(1L).productId(deletedProduct.getId()).build());
        deletedWish.delete();
        wishRepository.save(Wish.builder().memberId(2L).productId(product.getId()).build());

        // act
        List<WishOutput> result = wishQueryService.findAll(1L);

        // assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().productId()).isEqualTo(product.getId());
        assertThat(result.getFirst().productName()).isEqualTo("상품명");
        assertThat(result.getFirst().price()).isEqualTo(10000L);
        assertThat(result.getFirst().imageUrl()).isEqualTo("https://example.com/image.jpg");
    }

    @Test
    @DisplayName("위시리스트가 없는 회원은 빈 목록을 반환한다")
    void test02() {
        // arrange
        Long memberId = 1L;

        // act
        List<WishOutput> result = wishQueryService.findAll(memberId);

        // assert
        assertThat(result).isEmpty();
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
        List<WishOutput> result = wishQueryService.findAll(1L);

        // assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().productId()).isEqualTo(product.id());
        assertThat(result.getFirst().productName()).isEqualTo("삭제된 상품입니다.");
        assertThat(result.getFirst().price()).isNull();
        assertThat(result.getFirst().imageUrl()).isNull();
    }
}
