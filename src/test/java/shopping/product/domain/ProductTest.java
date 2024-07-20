package shopping.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import shopping.product.exception.ProductCreateException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("상품 이미지 경로가 null 이거나 공백일 경우 예외가 발생한다.")
    void validateImagePathNotNullOrEmpty(final String imagePath) {
        assertThatThrownBy(() -> new Product("productName", imagePath, 10, 1000))
                .isInstanceOf(ProductCreateException.class)
                .hasMessage("상품 이미지는 필수값 입니다.");
    }

    @Test
    @DisplayName("상품 수량이 0보다 작을 경우 예외가 발생한다.")
    void validateProductAmount() {
        assertThatThrownBy(() -> new Product("productName", "/image/path", -1, 1000))
                .isInstanceOf(ProductCreateException.class)
                .hasMessage("상품 수량은 0보다 커야합니다.");
    }

    @Test
    @DisplayName("상품 가격이 0보다 작을 경우 예외가 발생한다.")
    void validateProductPrice() {
        assertThatThrownBy(() -> new Product("productName", "/image/path", 10, -1000))
                .isInstanceOf(ProductCreateException.class)
                .hasMessage("상품 가격은 0보다 커야합니다.");
    }

}
