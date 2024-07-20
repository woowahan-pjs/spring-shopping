package shopping.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.product.exception.ProductCreateException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProductTest {
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("상품 이름이 null 이거나 공백일 경우 예외가 발생한다.")
    void validateProductNameNotNullOrEmpty(final String name) {
        assertThatThrownBy(() -> new Product(name, "/image/path", 10, 1000))
                .isInstanceOf(ProductCreateException.class)
                .hasMessage("상품 이름은 필수값 입니다.");
    }

    @Test
    @DisplayName("상품 이름이 15자를 초과할 경우 예외가 발생한다.")
    void validateProductNameLength() {
        assertThatThrownBy(() -> new Product("a".repeat(16), "/image/path", 10, 1000))
                .isInstanceOf(ProductCreateException.class)
                .hasMessage("상품 이름은 15자 이하여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "(", ")", "[", "]", "+", "-", "&", "/", "_"
    })
    @DisplayName("상품 이름에 포함될 수 있는 특수 문자가 있다.")
    void validateProductNameValidSpecialCharacters(final String specialCharacter) {
        assertDoesNotThrow(() -> new Product("test" + specialCharacter, "/image/path", 10, 1000));
    }

    @Test
    @DisplayName("상품 이름에 허용되지 않은 특수 문자가 포함될 경우 예외가 발생한다.")
    void validateProductNameInvalidSpecialCharacters() {
        assertThatThrownBy(() -> new Product("test@", "/image/path", 10, 1000))
                .isInstanceOf(ProductCreateException.class)
                .hasMessage("상품 이름에 허용되지 않는 특수 문자가 포함되어 있습니다.");
    }

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

    @Test
    @DisplayName("상품 이름이 유효하고, 이미지 경로, 수량 및 가격이 유효하면 예외가 발생하지 않는다.")
    void validateValidProduct() {
        new Product("productName", "/image/path", 10, 1000);
    }
}
