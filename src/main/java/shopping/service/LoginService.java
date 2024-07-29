package shopping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shopping.dto.LoginResponseDto;
import shopping.entity.UserDetail;
import shopping.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponseDto login(final String email, final String password) {
        final UserDetail userDetail = memberDetailsService.loadUserByEmail(email);

        if (userDetail == null || userDetail.isWrongPassword(password, passwordEncoder)) {
            throw new IllegalArgumentException("wrong email or password");
        }

        String token = jwtTokenProvider.createToken(userDetail.getId(), userDetail.getEmail());
        return new LoginResponseDto(token);
    }
}
