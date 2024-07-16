package shopping.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {
    @DisplayName("올바른 이메일 양식이 아닌 경우 예외를 발생시킨다.")
    @ParameterizedTest(name = "value = {0}")
    @ValueSource(strings = {
            "invalid",
            "invalid@",
            "invalid@invalid",
            "invalid@invalid.",
            "invalid@invalid.c"
    })
    @NullAndEmptySource
    void test(String emailValue) {
        // given
        // when
        // then
        assertThatThrownBy(() -> Email.from(emailValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바른 이메일 주소 양식이 아닙니다.");
    }
}