package shopping.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberTest {
    @DisplayName("Member 객체 생성 시 비밀번호는 암호화 된다.")
    @Test
    void of() {
        // given
        String rawPassword = "password";
        MemberCreate memberCreate = new MemberCreate("email@email.com", rawPassword);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // when
        Member member = Member.of(memberCreate, passwordEncoder);

        // then
        String encodedPassword = member.getPassword();
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }
}