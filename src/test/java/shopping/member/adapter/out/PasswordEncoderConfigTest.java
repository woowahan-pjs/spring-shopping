package shopping.member.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("[회원] 비밀번호 인코더 설정 단위 테스트")
class PasswordEncoderConfigTest {
    private final PasswordEncoderConfig passwordEncoderConfig = new PasswordEncoderConfig();

    @Test
    @DisplayName("PasswordEncoder Bean은 비밀번호를 해시하고 검증한다")
    void passwordEncoderBeanHashAndMatchPassword() {
        PasswordEncoder passwordEncoder = passwordEncoderConfig.passwordEncoder();
        String encodedPassword = passwordEncoder.encode("password123!");

        assertThat(encodedPassword).isNotBlank();
        assertThat(encodedPassword).isNotEqualTo("password123!");
        assertThat(passwordEncoder.matches("password123!", encodedPassword)).isTrue();
        assertThat(passwordEncoder.matches("different-password", encodedPassword)).isFalse();
    }
}
