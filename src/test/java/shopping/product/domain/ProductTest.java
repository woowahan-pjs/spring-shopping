package shopping.product.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {
    @DisplayName("상품 생성 시 이름은 15글자 까지만 입력할 수 있다.")
    @Test
    void nameLengthTest() {
        // given
        String name = "123456789123456";

        // when
        // then
        assertThatCode(() -> new Product(name, "https://github.com/SeolYoungKim/spring-shopping"))
                .doesNotThrowAnyException();
    }

    @DisplayName("상품 생성 시 15글자를 초과하는 이름을 입력하면 예외를 발생시킨다.")
    @Test
    void nameLengthTest2() {
        // given
        String name = "1234567891234567";

        // when
        // then
        assertThatThrownBy(() -> new Product(name, "https://github.com/SeolYoungKim/spring-shopping"))
                .isInstanceOf(InvalidProductNameLengthException.class);
    }

    @DisplayName("상품 생성 시 이름에 특수문자 ( ) [ ] + - & / _ 는 허용된다.")
    @ParameterizedTest(name = "name = {0}")
    @ValueSource(strings = {"(", ")", "[", "]", "+", "-", "&", "/", "_", "상품_", "product&"})
    void namePatternTest(String name) {
        // given
        // when
        // then
        assertThatCode(() -> new Product(name, "https://github.com/SeolYoungKim/spring-shopping"))
                .doesNotThrowAnyException();
    }

    @DisplayName("상품 생성 시 이름에 ( ) [ ] + - & / _ 외의 특수 문자 사용 시 예외를 발생시킨다.")
    @ParameterizedTest(name = "name = {0}")
    @ValueSource(strings = {"!", "@", "#", "$", "%", "^", "*", "`", "₩", "~", "?", "<", ">"})
    void namePatternTest2(String name) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Product(name, "https://github.com/SeolYoungKim/spring-shopping"))
                .isInstanceOf(InvalidProductNamePatternException.class);
    }
}