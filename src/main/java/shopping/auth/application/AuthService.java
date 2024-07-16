package shopping.auth.application;

import org.springframework.stereotype.Service;
import shopping.auth.domain.LoginMember;
import shopping.auth.dto.TokenRequest;
import shopping.auth.dto.TokenResponse;
import shopping.auth.infrastructure.JwtTokenProvider;


@Service
public class AuthService {
//    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

//    public TokenResponse login(TokenRequest request) {
//        Member member = memberRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new AuthorizationException(MEMBER_NOT_EXISTS));
//        member.checkPassword(request.getPassword());
//
//        String token = jwtTokenProvider.createToken(request.getEmail());
//        return new TokenResponse(token);
//    }

//    public LoginMember findMemberByToken(String credentials) {
//        if (!jwtTokenProvider.validateToken(credentials)) {
//            throw new AuthorizationException(INVALID_ACCESS_TOKEN);
//        }
//
//        String email = jwtTokenProvider.getPayload(credentials);
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new AuthorizationException(MEMBER_NOT_EXISTS));
//        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
//    }
}
