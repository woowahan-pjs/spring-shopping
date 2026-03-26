package shopping.product.service;
import org.junit.jupiter.api.BeforeEach;
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
import shopping.common.client.ProfanityChecker;
import shopping.product.domain.Product;
import shopping.product.service.dto.ProductRegisterInput;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductQueryServiceTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public ProfanityChecker fakeProfanityClient() {
            return new FakeProfanityChecker();
        }
    }

    @Autowired
    private ProductCommandService productCommandService;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private FakeProfanityChecker profanityClient;

    @BeforeEach
    void setUp() {
        profanityClient.setProfane(false);
    }

    @Test
    @DisplayName("상품 단건 조회가 가능하다")
    void test01() {
        // given
        Product saved = productCommandService.register(
            new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg"));

        // when
        Product response = productQueryService.getProduct(saved.getId());

        // then
        assertThat(response.getName()).isEqualTo("상품명");
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 시 예외가 발생한다")
    void test02() {
        assertThatThrownBy(() -> productQueryService.getProduct(999L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("상품 목록은 페이징으로 조회할 수 있다")
    void test03() {
        // arrange
        productCommandService.register(new ProductRegisterInput("상품1", 1000L, "https://example.com/1.jpg"));
        productCommandService.register(new ProductRegisterInput("상품2", 2000L, "https://example.com/2.jpg"));

        // act
        Page<Product> responses = productQueryService.findProductWithPage(
            PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id")));

        // assert
        assertThat(responses.getContent()).hasSize(1);
        assertThat(responses.getContent().getFirst().getName()).isEqualTo("상품2");
        assertThat(responses.getTotalElements()).isEqualTo(2);
        assertThat(responses.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("삭제한 상품은 단건 조회할 수 없다")
    void test04() {
        // given
        Product saved = productCommandService.register(
            new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg"));
        productCommandService.delete(saved.getId());

        // when & then
        assertThatThrownBy(() -> productQueryService.getProduct(saved.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("삭제한 상품은 상품 목록 조회에서 제외된다")
    void test05() {
        // arrange
        Product active = productCommandService.register(
            new ProductRegisterInput("상품1", 1000L, "https://example.com/1.jpg"));
        Product deleted = productCommandService.register(
            new ProductRegisterInput("상품2", 2000L, "https://example.com/2.jpg"));
        productCommandService.delete(deleted.getId());

        // act
        Page<Product> responses = productQueryService.findProductWithPage(
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")));

        // assert
        assertThat(responses.getContent()).hasSize(1);
        assertThat(responses.getContent().getFirst().getId()).isEqualTo(active.getId());
    }

    @Test
    @DisplayName("활성 상품 조회는 Optional로 반환한다")
    void test06() {
        // given
        Product saved = productCommandService.register(
            new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg"));

        // when
        var product = productQueryService.findProduct(saved.getId());

        // then
        assertThat(product).isPresent();
        assertThat(product.get().getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("삭제한 상품 조회는 빈 Optional을 반환한다")
    void test07() {
        // given
        Product saved = productCommandService.register(
            new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg"));
        productCommandService.delete(saved.getId());

        // when
        var product = productQueryService.findProduct(saved.getId());

        // then
        assertThat(product).isEmpty();
    }
}
