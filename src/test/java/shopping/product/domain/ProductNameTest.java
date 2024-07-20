package shopping.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.product.exception.ProductCreateException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProductNameTest {
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("상품 이름이 null 이거나 공백일 경우 예외가 발생한다.")
    void validateProductNameNotNullOrEmpty(final String name) {
        assertThatThrownBy(() -> new ProductName(name))
                .isInstanceOf(ProductCreateException.class)
                .hasMessage("상품 이름은 필수값 입니다.");
    }

    @Test
    @DisplayName("상품 이름이 15자를 초과할 경우 예외가 발생한다.")
    void validateProductNameLength() {
        assertThatThrownBy(() -> new ProductName("a".repeat(16)))
                .isInstanceOf(ProductCreateException.class)
                .hasMessage("상품 이름은 15자 이하여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "(", ")", "[", "]", "+", "-", "&", "/", "_"
    })
    @DisplayName("상품 이름에 포함될 수 있는 특수 문자가 있다.")
    void validateProductNameValidSpecialCharacters(final String specialCharacter) {
        assertDoesNotThrow(() -> new ProductName("test" + specialCharacter));
    }

    @Test
    @DisplayName("상품 이름에 허용되지 않은 특수 문자가 포함될 경우 예외가 발생한다.")
    void validateProductNameInvalidSpecialCharacters() {
        assertThatThrownBy(() -> new ProductName("test@"))
                .isInstanceOf(ProductCreateException.class)
                .hasMessage("상품 이름에 허용되지 않는 특수 문자가 포함되어 있습니다.");
    }
}
