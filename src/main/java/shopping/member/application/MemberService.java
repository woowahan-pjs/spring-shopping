package shopping.member.application;

import org.springframework.stereotype.Service;
import shopping.member.domain.Email;
import shopping.member.domain.Member;
import shopping.member.domain.MemberCreate;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void register(MemberCreate memberCreate) {
        if (memberRepository.existsByEmail(Email.from(memberCreate.email()))) {
            throw new AlreadyRegisteredEmailException();
        }

        Member member = Member.from(memberCreate);
        memberRepository.save(member);
    }
}
