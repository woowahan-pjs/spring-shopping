package shopping.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.auth.domain.Role;
import shopping.auth.dto.LoginRequest;
import shopping.auth.dto.LoginResponse;
import shopping.auth.dto.RegisterRequest;
import shopping.auth.dto.RegisterResponse;
import shopping.auth.dto.UserResponse;
import shopping.infra.security.JwtTokenProvider;
import shopping.infra.security.UserPrincipal;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원 가입 요청을 처리하고, 인증 토큰을 포함한 응답을 반환합니다.
     *
     * @param request 회원 가입 요청 정보를 담고 있는 RegisterRequest 객체입니다. 이 객체에는 이메일, 비밀번호, 권한 정보가 포함됩니다.
     * @return 회원 가입 성공 시 생성된 인증 토큰을 포함하는 RegisterResponse 객체를 반환합니다.
     */
    @Transactional
    public RegisterResponse register(final RegisterRequest request) {
        final Long userId = userService.register(request);

        return RegisterResponse.of(generateToken(userId, request.email(), request.role()));
    }

    /**
     * 회원 로그인 요청을 처리하고, 인증 토큰을 포함한 응답을 반환합니다.
     *
     * @param request 로그인 요청 정보를 담고 있는 LoginRequest 객체입니다. 이 객체에는 이메일과 비밀번호가 포함됩니다.
     * @return 인증 토큰을 포함한 LoginResponse 객체를 반환합니다.
     */
    @Transactional(readOnly = true)
    public LoginResponse login(final LoginRequest request) {
        final UserResponse response = userService.getUser(request.email(), request.password());

        return LoginResponse.of(generateToken(response.userId(), response.email(), response.role()));
    }

    private String generateToken(final Long userId, final String email, final Role role) {
        return jwtTokenProvider.generateToken(UserPrincipal.generate(userId, email, role));
    }
}
