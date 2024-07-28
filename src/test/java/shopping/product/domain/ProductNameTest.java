package shopping.product.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.fake.FakeProfanityChecker;
import shopping.product.exception.InvalidProductNameException;

@DisplayName("ProductName")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductNameTest {

    private ProfanityChecker profanityChecker = new FakeProfanityChecker();

    @Test
    void 이름이_null인_ProductName을_생성하면_예외를_던진다() {
        assertThatThrownBy(
                () -> new ProductName(null, profanityChecker))
                .isInstanceOf(InvalidProductNameException.class);
    }

    @Test
    void 비속어체커가_null인_ProductName을_생성하면_예외를_던진다() {
        assertThatThrownBy(
                () -> new ProductName("맥북", null))
                .isInstanceOf(InvalidProductNameException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"가나다라마바사아자차카파타하하하", "1234567890123456", "                "})
    void 이름의_길이가_15자를_넘어가는_ProductNmae을_생성하면_예외를_던진다(String name) {
        assertThatThrownBy(
                () -> new ProductName(name, profanityChecker))
                .isInstanceOf(InvalidProductNameException.class);
    }

    @ParameterizedTest
    @DisplayName("이름에 (), [], +, -, &, /, _ 을 제외한 특수문자가 들어가는 ProductName을 생성하면 예외를 던진다.")
    @ValueSource(strings = {"*맥북*", "{맥북}", "^맥북^"})
    void validatePattern(String name) {
        assertThatThrownBy(
                () -> new ProductName(name, profanityChecker))
                .isInstanceOf(InvalidProductNameException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"비속어맥북", "욕욕욕", "메롱롱롱"})
    void 이름에_비속어가_들어가는_ProductName을_생성하면_예외를_던진다(String name) {
        assertThatThrownBy(
                () -> new ProductName(name, profanityChecker))
                .isInstanceOf(InvalidProductNameException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"맥북", "아이패드", "아이폰"})
    void ProductName을_생성할_수_있다(String name) {
        assertThatNoException()
                .isThrownBy(() -> new ProductName(name, profanityChecker));
    }
}