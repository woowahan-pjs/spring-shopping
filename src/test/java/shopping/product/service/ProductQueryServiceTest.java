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
import shopping.product.service.dto.ProductOutput;
import shopping.product.service.dto.ProductRegisterInput;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductQueryServiceTest {

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
    private ProductQueryService productQueryService;

    @Autowired
    private FakeProfanityClient profanityClient;

    @BeforeEach
    void setUp() {
        profanityClient.setProfane(false);
    }

    @Test
    @DisplayName("상품 단건 조회가 가능하다")
    void test01() {
        // given
        ProductOutput saved = productCommandService.register(
            new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg"));

        // when
        ProductOutput response = productQueryService.findById(saved.id());

        // then
        assertThat(response.name()).isEqualTo("상품명");
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 시 예외가 발생한다")
    void test02() {
        assertThatThrownBy(() -> productQueryService.findById(999L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("상품 목록 조회가 가능하다")
    void test03() {
        // given
        productCommandService.register(new ProductRegisterInput("상품1", 1000L, "https://example.com/1.jpg"));
        productCommandService.register(new ProductRegisterInput("상품2", 2000L, "https://example.com/2.jpg"));

        // when
        List<ProductOutput> responses = productQueryService.findAll();

        // then
        assertThat(responses).hasSize(2);
    }
}
