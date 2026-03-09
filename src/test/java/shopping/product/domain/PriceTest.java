package shopping.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.infra.exception.ShoppingBusinessException;

class PriceTest {

    @Nested
    @DisplayName("Long 형식의 값을 Price로 만들 때,")
    class createFromLong {

        @Test
        @DisplayName("빈 값의 경우 null을 반환합니다.")
        void isNull() {
            // given
            final Long input = null;

            // when
            final Price result = Price.create(input);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("성공적으로 생성합니다.")
        void success() {
            // given
            final Long input = 3000L;

            // when
            final Price result = Price.create(input);

            // then
            assertThat(result.value()).isEqualTo(BigDecimal.valueOf(input));
        }
    }

    @Nested
    @DisplayName("문자열로 이루어진 값을 Price로 만들 때,")
    class createFromString {

        @ParameterizedTest
        @ValueSource(strings = {" ", "AAA", "ㄱㄴㄷ"})
        @DisplayName("숫작 아닌 값의 경우, 예외가 발생합니다.")
        void invalidInput() {
            // given
            final String input = "AAA";

            // when & then
            assertThatThrownBy(() -> Price.create(input))
                .isInstanceOf(ShoppingBusinessException.class)
                .hasMessage("잘못된 형식의 입력 값입니다.");
        }

        @Test
        @DisplayName("성공적으로 생성합니다.")
        void success() {
            // given
            final String input = "10000";

            // when
            final Price result = Price.create(input);

            // then
            assertThat(result.value()).isEqualTo(BigDecimal.valueOf(10000));
        }
    }
}
