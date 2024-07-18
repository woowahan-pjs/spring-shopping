package shopping.auth.application;

import org.springframework.stereotype.Service;
import shopping.auth.infrastructure.JwtTokenProvider;
import shopping.member.domain.MemberRepository;


@Service
public class AuthService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


}
