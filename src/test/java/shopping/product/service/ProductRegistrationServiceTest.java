package shopping.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import shopping.common.exception.ProfanityServiceUnavailableException;
import shopping.product.repository.ProductRepository;
import shopping.product.service.dto.ProductOutput;
import shopping.product.service.dto.ProductRegisterInput;

@SpringBootTest
@Transactional
class ProductRegistrationServiceTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public FakeProfanityChecker fakeProfanityClient() {
            return new FakeProfanityChecker();
        }
    }

    @Autowired
    private ProductRegistrationService productRegistrationService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FakeProfanityChecker profanityChecker;

    @BeforeEach
    void setUp() {
        profanityChecker.setProfane(false);
        profanityChecker.setUnavailable(false);
    }

    @Test
    @DisplayName("상품을 생성하면 ID가 부여된다")
    void test01() {
        // arrange
        ProductRegisterInput request = new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg");

        // act
        ProductOutput response = productRegistrationService.register(request);

        // assert
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("상품명");
    }

    @Test
    @DisplayName("상품명이 15자를 초과하면 예외가 발생한다")
    void test02() {
        // arrange
        ProductRegisterInput request = new ProductRegisterInput("열여섯자이상의긴상품명입니다초과", 10000L, "https://example.com/image.jpg");

        // act & assert
        assertThatThrownBy(() -> productRegistrationService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품명은 15자 이하이어야 합니다.");
    }

    @Test
    @DisplayName("상품명에 허용되지 않은 특수문자가 있으면 예외가 발생한다")
    void test03() {
        // arrange
        ProductRegisterInput request = new ProductRegisterInput("상품명!", 10000L, "https://example.com/image.jpg");

        // act & assert
        assertThatThrownBy(() -> productRegistrationService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품명에 허용되지 않은 특수문자가 포함되어 있습니다.");
    }

    @Test
    @DisplayName("상품명에 비속어가 포함되면 예외가 발생한다")
    void test04() {
        // arrange
        profanityChecker.setProfane(true);
        ProductRegisterInput request = new ProductRegisterInput("badword", 10000L, "https://example.com/image.jpg");

        // act & assert
        assertThatThrownBy(() -> productRegistrationService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품명에 비속어가 포함되어 있습니다.");
    }

    @Test
    @DisplayName("비속어 검증 서비스 장애 시 상품 생성은 실패하고 저장되지 않는다")
    void test05() {
        // arrange
        profanityChecker.setUnavailable(true);
        ProductRegisterInput request = new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg");

        // act & assert
        assertThatThrownBy(() -> productRegistrationService.register(request))
            .isInstanceOf(ProfanityServiceUnavailableException.class)
            .hasMessage("비속어 검증 서비스를 사용할 수 없습니다.");
        assertThat(productRepository.count()).isZero();
    }

    @Test
    @DisplayName("상품을 수정할 수 있다")
    void test06() {
        // arrange
        ProductOutput saved = productRegistrationService.register(
            new ProductRegisterInput("상품명", 10000L, "https://example.com/image.jpg"));

        // act
        ProductOutput response = productRegistrationService.update(
            saved.id(), new ProductRegisterInput("수정된상품명", 20000L, "https://example.com/new.jpg"));

        // assert
        assertThat(response.name()).isEqualTo("수정된상품명");
        assertThat(response.price()).isEqualTo(20000L);
    }
}
