package shopping.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("비밀번호를 암호화하면 평문과 달라진다")
    void test01() {
        // arrange
        String rawPassword = "password123";

        // act
        String encoded = authService.encodePassword(rawPassword);

        // assert
        assertThat(encoded).isNotEqualTo(rawPassword);
    }

    @Test
    @DisplayName("평문 비밀번호와 암호화된 비밀번호가 일치하면 예외가 발생하지 않는다")
    void test02() {
        // arrange
        String rawPassword = "password123";
        String encoded = authService.encodePassword(rawPassword);

        // act & assert
        authService.verifyPassword(rawPassword, encoded);
    }

    @Test
    @DisplayName("평문 비밀번호와 암호화된 비밀번호가 불일치하면 예외가 발생한다")
    void test03() {
        // arrange
        String rawPassword = "password123";
        String encoded = authService.encodePassword(rawPassword);

        // act & assert
        assertThatThrownBy(() -> authService.verifyPassword("wrongpassword", encoded))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}
