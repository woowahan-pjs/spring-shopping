package shopping.auth;

import org.springframework.stereotype.Component;

import shopping.member.MemberRepository;
import shopping.member.TokenProvider;

@Component
public class AuthenticationService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public AuthenticationService(TokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public Long extractMemberId(String authorization) {
        String token = authorization.replace("Bearer ", "");
        String email = tokenProvider.extractEmail(token);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다.")).getId();
    }
}
