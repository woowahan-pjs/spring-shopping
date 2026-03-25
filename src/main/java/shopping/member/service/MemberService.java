package shopping.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.auth.service.AuthService;
import shopping.auth.service.AuthTokens;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.member.adapter.in.api.LoginRequest;
import shopping.member.adapter.in.api.RegisterRequest;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public AuthTokens register(RegisterRequest request) {
        validateDuplicatedEmail(request.email());
        Member member = saveMember(request.email(), passwordEncoder.encode(request.password()));
        return authService.issueTokens(member.getId());
    }

    @Transactional(readOnly = true)
    public void requireActiveSeller(Long memberId) {
        findById(memberId).assertActiveSeller();
    }

    public AuthTokens login(LoginRequest request) {
        Member member = findByEmail(request.email());
        boolean passwordMatches = passwordEncoder.matches(request.password(), member.getPassword());
        member.assertCanLogin(passwordMatches);
        return authService.issueTokens(member.getId());
    }

    private void validateDuplicatedEmail(String email) {
        if (memberRepository.findByEmail(email).isEmpty()) {
            return;
        }
        throw new ApiException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
    }

    private Member saveMember(String email, String encodedPassword) {
        try {
            return memberRepository.save(Member.registerUser(email, encodedPassword));
        } catch (DataIntegrityViolationException exception) {
            throw new ApiException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_CREDENTIALS_INVALID));
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ErrorCode.AUTHENTICATION_TOKEN_INVALID));
    }
}
