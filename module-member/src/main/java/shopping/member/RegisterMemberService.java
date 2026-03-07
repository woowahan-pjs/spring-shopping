package shopping.member;

import java.util.UUID;

public class RegisterMemberService implements RegisterMember {

    private final MemberRepository memberRepository;

    public RegisterMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public String execute(String email, String password) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
        memberRepository.save(new Member(email, password));
        return UUID.randomUUID().toString();
    }
}
