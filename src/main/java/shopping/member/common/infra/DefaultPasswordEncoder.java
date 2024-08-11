package shopping.member.common.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import shopping.member.common.domain.PasswordEncoder;

@Component
@RequiredArgsConstructor
public class DefaultPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public String encode(final String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(final String rawPassword, final String password) {
        return passwordEncoder.matches(rawPassword, password);
    }
}
