package shopping.product.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.product.domain.ProductCreate;

class ProductServiceTest {
    @DisplayName("상품 생성 시, 이름에 비속어가 포함된 경우 예외를 발생시킨다.")
    @Test
    void create() {
        // given
        ProductService productService = new ProductService(new TestProductRepository(), new StubProfanityChecker(true));

        // when
        // then
        assertThatThrownBy(() -> productService.create(new ProductCreate("fuck", "http://localhost:8080", 1000)))
                .isInstanceOf(ContainsProfanityException.class);

    }
}