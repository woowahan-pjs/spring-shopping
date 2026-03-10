package shopping.application.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.domain.member.Member;
import shopping.domain.member.exception.LoginFailedException;
import shopping.domain.repository.MemberRepository;
import shopping.dto.LoginRequest;
import shopping.dto.LoginResponse;
import shopping.dto.SignUpRequest;
import shopping.infrastructure.auth.JwtTokenProvider;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Long signUp(SignUpRequest request) {
        if(memberRepository.existByEmail(request.email())) {
            throw new IllegalArgumentException(request.email());
        }

        String encodePassword = passwordEncoder.encode(request.password());
        Member member = Member.create(request.email(), encodePassword);

        return memberRepository.save(member).getId();
    }

    @Transactional(readOnly=true)
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new LoginFailedException(request.email()));

        if(!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new LoginFailedException(request.email());
        }

        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRole());

        return new LoginResponse(token);
    }
}
