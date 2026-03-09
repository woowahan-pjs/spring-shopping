package shopping.product.service;

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
import shopping.product.repository.ProductRepository;
import shopping.product.service.dto.ProductOutput;
import shopping.product.service.dto.ProductRegisterInput;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductCommandServiceTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public ProfanityClient fakeProfanityClient() {
            return new FakeProfanityClient();
        }
    }

    @Autowired
    private ProductCommandService productCommandService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FakeProfanityClient profanityClient;

    @BeforeEach
    void setUp() {
        profanityClient.setProfane(false);
    }

    @Test
    @DisplayName("상품을 생성하면 ID가 부여된다")
    void test01() {
        // given
        ProductRegisterInput request = new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg");

        // when
        ProductOutput response = productCommandService.register(request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("상품명");
    }

    @Test
    @DisplayName("상품명이 15자를 초과하면 예외가 발생한다")
    void test02() {
        // given
        ProductRegisterInput request = new ProductRegisterInput("열여섯자이상의긴상품명입니다초과", 10000L, "https://example.com/image.jpg");

        // when & then
        assertThatThrownBy(() -> productCommandService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품명은 15자 이하이어야 합니다.");
    }

    @Test
    @DisplayName("상품명에 허용되지 않은 특수문자가 있으면 예외가 발생한다")
    void test03() {
        // given
        ProductRegisterInput request = new ProductRegisterInput("상품명!", 10000L, "https://example.com/image.jpg");

        // when & then
        assertThatThrownBy(() -> productCommandService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품명에 허용되지 않은 특수문자가 포함되어 있습니다.");
    }

    @Test
    @DisplayName("상품명에 비속어가 포함되면 예외가 발생한다")
    void test04() {
        // given
        profanityClient.setProfane(true);
        ProductRegisterInput request = new ProductRegisterInput("badword", 10000L, "https://example.com/image.jpg");

        // when & then
        assertThatThrownBy(() -> productCommandService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품명에 비속어가 포함되어 있습니다.");
    }

    @Test
    @DisplayName("가격이 0 이하이면 예외가 발생한다")
    void test05() {
        // given
        ProductRegisterInput request = new ProductRegisterInput("상품명", 0L, "https://example.com/image.jpg");

        // when & then
        assertThatThrownBy(() -> productCommandService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격은 양수이어야 합니다.");
    }

    @Test
    @DisplayName("상품을 수정할 수 있다")
    void test06() {
        // given
        ProductOutput saved = productCommandService.register(
            new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg"));

        // when
        ProductOutput response = productCommandService.update(
            saved.id(), new ProductRegisterInput("수정된상품명", 20000L, "https://example.com/new.jpg"));

        // then
        assertThat(response.name()).isEqualTo("수정된상품명");
        assertThat(response.price()).isEqualTo(20000L);
    }

    @Test
    @DisplayName("상품을 삭제할 수 있다")
    void test07() {
        // given
        ProductOutput saved = productCommandService.register(
            new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg"));

        // when
        productCommandService.delete(saved.id());

        // then
        assertThat(productRepository.findById(saved.id())).isPresent();
        assertThat(productRepository.findById(saved.id()).get().isDeleted()).isTrue();
    }
}
