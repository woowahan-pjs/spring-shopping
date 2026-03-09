package shopping.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shopping.auth.adapter.out.JwtTokenProvider;
import shopping.auth.domain.RefreshToken;
import shopping.auth.domain.JwtSubject;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRepository;

@Service
@Transactional
public class AuthService {
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenManager refreshTokenManager;

    public AuthService(
            JwtTokenProvider jwtTokenProvider,
            MemberRepository memberRepository,
            RefreshTokenManager refreshTokenManager
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.refreshTokenManager = refreshTokenManager;
    }

    public AuthTokens issueTokens(Long memberId) {
        String accessToken = jwtTokenProvider.create(memberId);
        String refreshToken = refreshTokenManager.issue(memberId);
        return new AuthTokens(accessToken, refreshToken);
    }

    public AuthTokens refresh(String refreshToken) {
        RefreshToken savedRefreshToken = refreshTokenManager.getValidToken(refreshToken);
        Member member = findRefreshableMember(savedRefreshToken.getMemberId());

        String accessToken = jwtTokenProvider.create(member.getId());
        String rotatedRefreshToken = refreshTokenManager.rotate(savedRefreshToken);
        return new AuthTokens(accessToken, rotatedRefreshToken);
    }

    public Long authenticate(String authorizationHeader) {
        String token = extractBearerToken(authorizationHeader);
        JwtSubject subject = jwtTokenProvider.parse(token);
        return subject.memberId();
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new ApiException(ErrorCode.AUTHORIZATION_HEADER_REQUIRED);
        }
        if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new ApiException(ErrorCode.AUTHORIZATION_HEADER_INVALID);
        }
        return authorizationHeader.substring(BEARER_PREFIX.length()).trim();
    }

    private Member findRefreshableMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ErrorCode.REFRESH_TOKEN_INVALID));
        if (member.isActive()) {
            return member;
        }
        throw new ApiException(ErrorCode.REFRESH_TOKEN_INVALID);
    }
}
