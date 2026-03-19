package shopping.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.domain.product.exception.ProductNameBlankException;
import shopping.domain.product.exception.ProductNameInvalidCharacterException;
import shopping.domain.product.exception.ProductNameLengthExceededException;

import static org.junit.jupiter.api.Assertions.*;

class ProductNameTest {

    @ParameterizedTest
    @ValueSource(strings = {"ValidName", "한글이름", "Name 123", "Option(1)", "[New]Item", "A+B", "A-B", "A&B", "A/B", "A_B"})
    @DisplayName("유효한 상품명인 경우")
    void shouldCreateProductName(String name) {
        assertDoesNotThrow(() -> new ProductName(name));
    }

    @Test
    @DisplayName("공백 혹은 빈값인 상품명을 입력할 경우 예외")
    void shouldThrowException_whenNameIsNullOrEmpty() {
        assertThrows(ProductNameBlankException.class, () -> new ProductName(null));
        assertThrows(ProductNameBlankException.class, () -> new ProductName(""));
        assertThrows(ProductNameBlankException.class, () -> new ProductName("   "));
    }

    @Test
    @DisplayName("글자수를 초과한 상품명을 입력할 경우 예외")
    void shouldThrowException_whenNameTooLong() {
        String longName = "1234567890123456"; // 16 characters
        assertThrows(ProductNameLengthExceededException.class, () -> new ProductName(longName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name(", "Name)", "Name[", "Name]", "Name+", "Name-", "Name&", "Name/", "Name_"})
    @DisplayName("허용되는 특수문자 검증")
    void shouldValidateSpecialChars(String name) {
        assertDoesNotThrow(() -> new ProductName(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name@", "Name!", "Name#", "Name$", "Name%", "Name^", "Name*", "Name=", "Name{", "Name}"})
    @DisplayName("허용되지 않는 특수문자가 포함된 상품명을 입력할 경우 예외")
    void shouldThrowException_whenNameContainsInvalidChars(String invalidName) {
        assertThrows(ProductNameInvalidCharacterException.class, () -> new ProductName(invalidName));
    }
}