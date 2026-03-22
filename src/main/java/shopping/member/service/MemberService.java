package shopping.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.auth.JwtProvider;
import shopping.common.exception.EmailAlreadyExistsException;
import shopping.common.exception.LoginFailedException;
import shopping.member.domain.Member;
import shopping.member.dto.AuthResponse;
import shopping.member.dto.LoginRequest;
import shopping.member.dto.RegisterRequest;
import shopping.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.create(request.getEmail(), encodedPassword);

        memberRepository.save(member);

        String token = jwtProvider.createToken(member.getEmail());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new LoginFailedException();
        }

        String token = jwtProvider.createToken(member.getEmail());

        return new AuthResponse(token);
    }
}
