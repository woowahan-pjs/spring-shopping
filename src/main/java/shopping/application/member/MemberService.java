package shopping.application.member;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shopping.domain.member.Member;
import shopping.domain.repository.MemberRepository;
import shopping.dto.SignUpRequest;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long signUp(SignUpRequest request) {
        if(memberRepository.existByEmail(request.email())) {
            throw new IllegalArgumentException(request.email());
        }

        String encodePassword = passwordEncoder.encode(request.password());
        Member member = Member.create(request.email(), encodePassword);

        return memberRepository.save(member).getId();
    }
}
