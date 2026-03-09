package shopping.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import org.springframework.stereotype.Component;
import shopping.auth.AuthProperties;
import shopping.auth.domain.RefreshToken;
import shopping.auth.domain.RefreshTokenRepository;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@Component
public class RefreshTokenManager {
    private static final int REFRESH_TOKEN_BYTE_LENGTH = 32;

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenValidityDays;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenManager(RefreshTokenRepository refreshTokenRepository, AuthProperties authProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenValidityDays = authProperties.getRefreshTokenValidityDays();
    }

    public String issue(Long memberId) {
        String rawRefreshToken = generateRefreshToken();
        RefreshToken refreshToken = RefreshToken.issue(memberId, hash(rawRefreshToken), expiresAt());
        refreshTokenRepository.save(refreshToken);
        return rawRefreshToken;
    }

    public RefreshToken getValidToken(String rawRefreshToken) {
        validateRawToken(rawRefreshToken);
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(hash(rawRefreshToken))
                .orElseThrow(() -> new ApiException(ErrorCode.REFRESH_TOKEN_INVALID));
        if (!refreshToken.isExpired(LocalDateTime.now())) {
            return refreshToken;
        }

        refreshTokenRepository.delete(refreshToken);
        throw new ApiException(ErrorCode.REFRESH_TOKEN_INVALID);
    }

    public String rotate(RefreshToken refreshToken) {
        String rotatedRefreshToken = generateRefreshToken();
        refreshToken.rotate(hash(rotatedRefreshToken), expiresAt());
        return rotatedRefreshToken;
    }

    private void validateRawToken(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_REQUIRED);
        }
    }

    private String generateRefreshToken() {
        byte[] bytes = new byte[REFRESH_TOKEN_BYTE_LENGTH];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable.", exception);
        }
    }

    private LocalDateTime expiresAt() {
        return LocalDateTime.now().plusDays(refreshTokenValidityDays);
    }
}
