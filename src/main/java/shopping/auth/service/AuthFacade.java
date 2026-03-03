package shopping.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shopping.auth.dto.RegisterRequest;
import shopping.auth.dto.RegisterResponse;
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

        final String token =
                jwtTokenProvider.generateToken(
                        UserPrincipal.generate(userId, request.email(), request.role()));

        return RegisterResponse.of(token);
    }
}
