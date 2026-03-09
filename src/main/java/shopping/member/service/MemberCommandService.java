package shopping.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.auth.AuthService;
import shopping.member.domain.Member;
import shopping.member.repository.MemberRepository;
import shopping.member.service.dto.MemberLoginInput;
import shopping.member.service.dto.MemberRegisterInput;
import shopping.member.service.dto.TokenOutput;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {
    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;
    private final AuthService authService;

    public TokenOutput register(MemberRegisterInput input) {
        verifyEmail(input.email());
        Member saved = memberRepository.save(Member.builder()
                                                   .email(input.email())
                                                   .password(authService.encodePassword(input.password()))
                                                   .build());
        String token = authService.createToken(saved.getId());
        return new TokenOutput(token);
    }

    public TokenOutput login(MemberLoginInput input) {
        Member member = memberQueryService.getMemberByEmail(input.email());
        authService.verifyPassword(input.password(), member.getPassword());
        String token = authService.createToken(member.getId());
        return new TokenOutput(token);
    }

    private void verifyEmail(String email) {
        if (memberQueryService.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }
}
