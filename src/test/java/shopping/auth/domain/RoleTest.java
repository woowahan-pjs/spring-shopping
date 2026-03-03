package shopping.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import shopping.infra.exception.ShoppingBusinessException;

class RoleTest {

    @Nested
    @DisplayName("문자열에서 Role로 변환할 때,")
    class convert {

        @ParameterizedTest(name = "{index} : input={0}, expected={1}")
        @MethodSource("validRoles")
        @DisplayName("유효한 문자열의 경우 성공적으로 변환합니다.")
        void success(final String name, final Role expected) {
            // when
            final Role result = Role.convert(name);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"admin", "customer", "user"})
        void invalid(final String name) {
            // when & then
            assertThatThrownBy(() -> Role.convert(name))
                    .isInstanceOf(ShoppingBusinessException.class)
                    .hasMessage("유효하지 않은 권한입니다.");
        }

        static Stream<Arguments> validRoles() {
            return Stream.of(
                    Arguments.of("ADMIN", Role.ADMIN), Arguments.of("CUSTOMER", Role.CUSTOMER));
        }
    }
}
