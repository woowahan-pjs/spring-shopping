package shopping.auth.application;

import org.springframework.stereotype.Service;
import shopping.auth.application.dto.AuthResponse;
import shopping.common.exception.UnauthorizedException;

@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final UserDetailsService userDetailsService, final JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse login(final String email, final String password) {
        final UserDetail userDetail = userDetailsService.loadUserByEmail(email);

        if (userDetail == null || userDetail.isPasswordMismatch(password)) {
            throw new UnauthorizedException("아이디와 비밀번호를 확인해주세요.");
        }

        return new AuthResponse(jwtTokenProvider.createToken(userDetail.getId(), userDetail.getEmail()));
    }

}
