package shopping.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shopping.auth.token.JwtSubject;
import shopping.auth.token.JwtTokenProvider;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public String issueToken(Long memberId) {
        return jwtTokenProvider.create(memberId);
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
        if (authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        }
        throw new ApiException(ErrorCode.AUTHORIZATION_HEADER_INVALID);
    }
}
