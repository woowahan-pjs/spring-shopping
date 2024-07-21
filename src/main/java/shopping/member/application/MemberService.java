package shopping.member.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shopping.member.domain.Email;
import shopping.member.domain.Member;
import shopping.member.domain.MemberCreate;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(MemberCreate memberCreate) {
        if (isAlreadyRegistered(Email.from(memberCreate.email()))) {
            throw new AlreadyRegisteredEmailException();
        }

        Member member = Member.of(memberCreate, passwordEncoder);
        memberRepository.save(member);
    }

    public boolean isAlreadyRegistered(Email email) {
        return memberRepository.existsByEmail(email);
    }

    public Member getByEmail(String email) {
        return memberRepository.findByEmail(Email.from(email))
                .orElseThrow(() -> new MemberNotFoundException(email));
    }
}
