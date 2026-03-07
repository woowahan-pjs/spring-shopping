package shopping.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("회원을 생성한다")
    void createMember() {
        Member member = new Member("email", "password");

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
        assertThatThrownBy(() -> new Member("email", password))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
