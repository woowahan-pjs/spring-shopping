package shopping.member.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.auth.application.AuthService;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.member.api.LoginRequest;
import shopping.member.api.RegisterRequest;
import shopping.member.api.TokenResponse;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRole;
import shopping.member.domain.MemberRepository;
import shopping.member.domain.MemberStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthService authService;

    public TokenResponse register(RegisterRequest request) {
        validateDuplicatedEmail(request.email());
        String encoded = encodePassword(request.password());
        Member member = saveMember(request.email(), encoded);
        String token = authService.issueToken(member.getId());
        return new TokenResponse(token);
    }

    @Transactional(readOnly = true)
    public void requireActiveSeller(Long memberId) {
        Member member = findById(memberId);
        validateActiveSeller(member);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        Member member = findByEmail(request.email());
        validateMemberState(member);
        validatePassword(member, request.password());
        String token = authService.issueToken(member.getId());
        return new TokenResponse(token);
    }

    private void validateDuplicatedEmail(String email) {
        if (memberRepository.findByEmail(email).isEmpty()) {
            return;
        }
        throw new ApiException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
    }

    private Member saveMember(String email, String encodedPassword) {
        try {
            return memberRepository.save(Member.create(email, encodedPassword, MemberStatus.ACTIVE, MemberRole.USER));
        } catch (DataIntegrityViolationException exception) {
            throw new ApiException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_CREDENTIALS_INVALID));
    }

    private void validateMemberState(Member member) {
        if (member.isActive()) {
            return;
        }
        throw new ApiException(ErrorCode.MEMBER_CREDENTIALS_INVALID);
    }

    private void validatePassword(Member member, String rawPassword) {
        String encoded = encodePassword(rawPassword);
        if (member.getPassword().equals(encoded)) {
            return;
        }
        throw new ApiException(ErrorCode.MEMBER_CREDENTIALS_INVALID);
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_SELLER_REQUIRED));
    }

    private void validateActiveSeller(Member member) {
        if (!member.isActive()) {
            throw new ApiException(ErrorCode.MEMBER_INACTIVE_FORBIDDEN);
        }
        if (member.isSeller()) {
            return;
        }
        throw new ApiException(ErrorCode.MEMBER_SELLER_REQUIRED);
    }

    private String encodePassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable.", exception);
        }
    }
}
