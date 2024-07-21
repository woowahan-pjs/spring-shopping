package shopping.token.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shopping.member.application.MemberService;
import shopping.member.domain.Member;
import shopping.token.domain.Token;
import shopping.token.domain.TokenProvider;

@Service
public class TokenService {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public TokenService(MemberService memberService, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public Token generate(LoginRequest loginRequest) {
        Member member = memberService.getByEmail(loginRequest.email());
        validatePassword(loginRequest, member);
        return tokenProvider.generate(member.getEmail());
    }

    private void validatePassword(LoginRequest loginRequest, Member member) {
        if (isNotMatchedPassword(loginRequest, member)) {
            throw new NotMatchedPasswordException();
        }
    }

    private boolean isNotMatchedPassword(LoginRequest loginRequest, Member member) {
        return !passwordEncoder.matches(loginRequest.password(), member.getPassword());
    }

    public String extractEmail(String authorizationHeader) {
        return tokenProvider.extractEmail(authorizationHeader);
    }
}
