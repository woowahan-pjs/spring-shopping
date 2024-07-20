package shopping.auth.application;

import org.springframework.stereotype.Service;
import shopping.auth.domain.LoginMember;
import shopping.auth.dto.TokenRequest;
import shopping.auth.dto.TokenResponse;
import shopping.auth.infrastructure.JwtTokenProvider;
import shopping.constant.enums.YesNo;
import shopping.exception.AuthorizationException;
import shopping.member.application.MemberService;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;


@Service
public class AuthService {

    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberService.findMemberByEmailAndDelYn(request.getEmail(), YesNo.N);
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException("올바르지 않은 토큰입니다.");
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberService.findMemberByEmailAndDelYn(email, YesNo.N);
        return new LoginMember(member.getMbrSn(), member.getEmail());
    }


}
