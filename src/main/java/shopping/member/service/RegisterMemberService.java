package shopping.member.service;

import shopping.member.domain.*;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RegisterMemberService implements RegisterMember {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public RegisterMemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
            TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String execute(String email, String password) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
        memberRepository.save(new Member(email, passwordEncoder.encode(password)));
        return tokenProvider.createToken(email);
    }
}
