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
import shopping.product.api.dto.ProductDetailResponse;
import shopping.product.api.dto.ProductRegisterRequest;
import shopping.product.repository.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public FakeProductNameValidator fakeProductNameValidator() {
            return new FakeProductNameValidator();
        }
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FakeProductNameValidator productNameValidator;

    @BeforeEach
    void setUp() {
        productNameValidator.setProfane(false);
    }

    @Test
    @DisplayName("상품을 생성하면 ID가 부여된다")
    void test01() {
        // given
        ProductRegisterRequest request = new ProductRegisterRequest("상품명", 10000L, "https://example.com/image.jpg");

        // when
        ProductDetailResponse response = productService.register(request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("상품명");
    }

    @Test
    @DisplayName("상품명이 15자를 초과하면 예외가 발생한다")
    void test02() {
        // given
        ProductRegisterRequest request = new ProductRegisterRequest("열여섯자이상의긴상품명입니다초과", 10000L, "https://example.com/image.jpg");

        // when & then
        assertThatThrownBy(() -> productService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품명은 15자 이하이어야 합니다.");
    }

    @Test
    @DisplayName("상품명에 허용되지 않은 특수문자가 있으면 예외가 발생한다")
    void test03() {
        // given
        ProductRegisterRequest request = new ProductRegisterRequest("상품명!", 10000L, "https://example.com/image.jpg");

        // when & then
        assertThatThrownBy(() -> productService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품명에 허용되지 않은 특수문자가 포함되어 있습니다.");
    }

    @Test
    @DisplayName("상품명에 비속어가 포함되면 예외가 발생한다")
    void test04() {
        // given
        productNameValidator.setProfane(true);
        ProductRegisterRequest request = new ProductRegisterRequest("badword", 10000L, "https://example.com/image.jpg");

        // when & then
        assertThatThrownBy(() -> productService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품명에 비속어가 포함되어 있습니다.");
    }

    @Test
    @DisplayName("가격이 0 이하이면 예외가 발생한다")
    void test05() {
        // given
        ProductRegisterRequest request = new ProductRegisterRequest("상품명", 0L, "https://example.com/image.jpg");

        // when & then
        assertThatThrownBy(() -> productService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격은 양수이어야 합니다.");
    }

    @Test
    @DisplayName("상품 단건 조회가 가능하다")
    void test06() {
        // given
        ProductDetailResponse saved = productService.register(
            new ProductRegisterRequest("상품명", 10000L, "https://example.com/image.jpg"));

        // when
        ProductDetailResponse response = productService.findById(saved.id());

        // then
        assertThat(response.name()).isEqualTo("상품명");
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 시 예외가 발생한다")
    void test07() {
        assertThatThrownBy(() -> productService.findById(999L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("상품 목록 조회가 가능하다")
    void test08() {
        // given
        productService.register(new ProductRegisterRequest("상품1", 1000L, "https://example.com/1.jpg"));
        productService.register(new ProductRegisterRequest("상품2", 2000L, "https://example.com/2.jpg"));

        // when
        List<ProductDetailResponse> responses = productService.findAll();

        // then
        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("상품을 수정할 수 있다")
    void test09() {
        // given
        ProductDetailResponse saved = productService.register(
            new ProductRegisterRequest("상품명", 10000L, "https://example.com/image.jpg"));

        // when
        ProductDetailResponse response = productService.update(
            saved.id(), new ProductRegisterRequest("수정된상품명", 20000L, "https://example.com/new.jpg"));

        // then
        assertThat(response.name()).isEqualTo("수정된상품명");
        assertThat(response.price()).isEqualTo(20000L);
    }

    @Test
    @DisplayName("상품을 삭제할 수 있다")
    void test10() {
        // given
        ProductDetailResponse saved = productService.register(
            new ProductRegisterRequest("상품명", 10000L, "https://example.com/image.jpg"));

        // when
        productService.delete(saved.id());

        // then
        assertThat(productRepository.findById(saved.id())).isEmpty();
    }
}
