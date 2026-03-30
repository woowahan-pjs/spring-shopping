package shopping.member.service;

import shopping.member.domain.*;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RegisterMemberService implements RegisterMember {

    private final MemberRepository memberRepository;
    private final PasswordFactory passwordFactory;
    private final TokenProvider tokenProvider;

    public RegisterMemberService(MemberRepository memberRepository, PasswordFactory passwordFactory,
            TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordFactory = passwordFactory;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String execute(String email, String password) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Password encodedPassword = passwordFactory.create(password);
        Member member = memberRepository.save(new Member(email, encodedPassword));
        return tokenProvider.createToken(member.getId());
    }
}
