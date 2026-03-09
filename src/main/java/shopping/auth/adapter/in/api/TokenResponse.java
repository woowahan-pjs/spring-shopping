package shopping.auth.adapter.in.api;

import shopping.auth.service.AuthTokens;

public record TokenResponse(String token) {
    public static TokenResponse from(AuthTokens tokens) {
        return new TokenResponse(tokens.accessToken());
    }
}
