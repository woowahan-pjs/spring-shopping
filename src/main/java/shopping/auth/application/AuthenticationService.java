package shopping.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shopping.member.application.MemberService;
import shopping.member.domain.Member;
import shopping.token.application.TokenService;
import shopping.token.domain.Token;

@Service
public class AuthenticationService {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthenticationService(MemberService memberService, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public Token authenticate(LoginRequest loginRequest) {
        Member member = memberService.getByEmail(loginRequest.email());
        validatePassword(loginRequest, member);
        return tokenService.generate(member.getEmail());
    }

    private void validatePassword(LoginRequest loginRequest, Member member) {
        if (isNotMatchedPassword(loginRequest, member)) {
            throw new NotMatchedPasswordException();
        }
    }

    private boolean isNotMatchedPassword(LoginRequest loginRequest, Member member) {
        return !passwordEncoder.matches(loginRequest.password(), member.getPassword());
    }
}
