package shopping.product.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}