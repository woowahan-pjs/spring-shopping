package shopping.member.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.fake.FakePasswordEncoder;

@DisplayName("Password")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PasswordTest {

    private PasswordEncoder passwordEncoder = new FakePasswordEncoder();

    @Test
    void Password를_생성할_수_있다() {
        assertThatNoException()
                .isThrownBy(() -> new Password("1234", passwordEncoder));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "qwer", "password"})
    void Password가_맞는지_확인할_수_있다(String rawPassword) {
        final Password password = new Password(rawPassword, passwordEncoder);

        assertThat(password.isMatch(rawPassword, passwordEncoder)).isTrue();
    }
}