package shopping.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("회원 도메인")
class MemberTest {

    @Test
    @DisplayName("회원을 생성한다")
    void createMember() {
        Member member = new Member("test@gmail.com", "password");

        assertThat(member).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("이메일은 필수값이다")
    @NullAndEmptySource
    void invalidEmail(String email) {
        assertThatThrownBy(() -> new Member(email, "password"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("패스워드는 필수값이다")
    @NullAndEmptySource
    void invalidPassword(String password) {
        assertThatThrownBy(() -> new Member("test@gmail.com", password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("패스워드는 8~20자다.")
    @ValueSource(strings = {"12345678", "12345678901234567890"})
    void validPasswordLength(String password) {
        assertThatCode(() -> new Member("test@gmail.com", password))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("패스워드는 8~20자가 아니면 예외.")
    @ValueSource(strings = {"1234567", "123456789012345678901"})
    void invalidPasswordLength(String password) {
        assertThatThrownBy(() -> new Member("test@gmail.com", password))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
