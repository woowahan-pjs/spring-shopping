package shopping.learning;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {
    @DisplayName("PasswordEncoder로 인코딩된 패스워드와 입력된 패스워드가 일치하면 true를 반환한다.")
    @Test
    void test() {
        // given
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        String inputPassword = "password";

        // when
        boolean matches = passwordEncoder.matches(inputPassword, encodedPassword);

        // then
        assertThat(matches).isTrue();
    }

    @DisplayName("PasswordEncoder로 인코딩된 패스워드와 입력된 패스워드가 일치하지 않으면 false를 반환한다.")
    @Test
    void test2() {
        // given
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        String inputPassword = "패스워드";

        // when
        boolean matches = passwordEncoder.matches(inputPassword, encodedPassword);

        // then
        assertThat(matches).isFalse();
    }
}
