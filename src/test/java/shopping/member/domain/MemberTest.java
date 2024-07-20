package shopping.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.member.exception.InvalidMemberException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MemberTest {

    @Test
    @DisplayName("유효한 이메일과 비밀번호로 회원을 생성할 수 있다.")
    void createMemberWithValidEmailAndPassword() {
        final Member member = new Member("test@test.com", "password");

        assertSoftly(softly -> {
            softly.assertThat(member.getEmail()).isEqualTo("test@test.com");
            softly.assertThat(member.getPassword()).isEqualTo("password");
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid",
            "invalid@email",
            "invalid.email",
            "invalid@email.",
            "invalid@.com",
            "@email.com",
    })
    @DisplayName("잘못된 형식의 이메일로 회원을 생성하면 예외가 발생한다.")
    void createMemberWithInvalidEmail(final String email) {
        assertThatThrownBy(() -> new Member(email, "password"))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage("유효하지 않은 이메일 입니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이메일 없이 회원을 생성하면 예외가 발생한다.")
    void createMemberWithEmptyEmail(final String email) {
        assertThatThrownBy(() -> new Member(email, "password"))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage("회원 이메일은 필수값 입니다.");
    }


    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("비밀번호없이 회원을 생성하면 예외가 발생한다.")
    void createMemberWithEmptyPassword(final String password) {
        assertThatThrownBy(() -> new Member("test@test.com", password))
                .isInstanceOf(InvalidMemberException.class)
                .hasMessage("회원 비밀번호는 필수값 입니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {
            "test:test:true",
            "test:test2:false"
    }, delimiter = ':')
    @DisplayName("비밀번호가 같은지 여부를 반환받을 수 있다.")
    void checkPassword(final String actual, final String input, final boolean expected) {
        final Member member = new Member("test@test.com", actual);

        final boolean result = member.checkPassword(input);

        assertThat(result).isEqualTo(expected);
    }
}
