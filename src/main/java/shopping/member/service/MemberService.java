package shopping.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.auth.AuthService;
import shopping.member.domain.Member;
import shopping.member.api.dto.MemberLoginRequest;
import shopping.member.api.dto.MemberRegisterRequest;
import shopping.member.api.dto.TokenResponse;
import shopping.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthService authService;

    public TokenResponse register(MemberRegisterRequest request) {
        verifyEmail(request.email());
        Member saved = memberRepository.save(Member.builder()
                                                   .email(request.email())
                                                   .password(authService.encodePassword(request.password()))
                                                   .build());
        String token = authService.createToken(saved.getId());
        return new TokenResponse(token);
    }

    public TokenResponse login(MemberLoginRequest request) {
        Member member = getMemberByEmail(request.email());
        authService.verifyPassword(request.password(), member.getPassword());
        String token = authService.createToken(member.getId());
        return new TokenResponse(token);
    }

    private void verifyEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                               .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
    }
}
