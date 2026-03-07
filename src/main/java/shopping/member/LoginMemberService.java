package shopping.member;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class LoginMemberService implements LoginMember {

    private final MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public String execute(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));
        if (!member.getPassword().equals(password)) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
        return UUID.randomUUID().toString();
    }
}
