package shopping.member.common.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.common.auth.token.TokenGenerator;
import shopping.member.common.domain.Member;
import shopping.member.common.domain.MemberRepository;
import shopping.member.common.domain.MemberRole;
import shopping.member.common.domain.Password;
import shopping.member.common.domain.PasswordEncoder;
import shopping.member.common.exception.InvalidEmailException;
import shopping.member.common.exception.InvalidMemberException;
import shopping.member.common.exception.InvalidPasswordException;
import shopping.member.common.exception.NotFoundMemberException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    public void validateEmail(final String email) {
        final Optional<Member> existMember = memberRepository.findByEmail(email);
        if (existMember.isPresent()) {
            throw new InvalidEmailException("이미 존재하는 이메일 입니다. " + email);
        }
    }

    public Password encodePassword(final String rawPassword) {
        return new Password(rawPassword, passwordEncoder);
    }

    public String login(final String email, final String rawPassword, final MemberRole role) {
        final Member member = findMember(email, role);

        if (!member.isValidPassword(rawPassword, passwordEncoder)) {
            throw new InvalidPasswordException("비밀번호가 틀렸습니다.");
        }

        return tokenGenerator.generate(member.getEmail(), member.getMemberRole());
    }

    public Member findMember(String email, MemberRole role) {
        final Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberException("찾을 수 없는 사용자입니다. " + email));

        if (!member.isValidRole(role)) {
            throw new InvalidMemberException("권한이 없는 회원입니다.");
        }

        return member;
    }
}
