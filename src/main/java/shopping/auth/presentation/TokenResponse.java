package shopping.auth.presentation;

import shopping.token.domain.Token;

public record TokenResponse(String token) {
    public static TokenResponse from(Token token) {
        return new TokenResponse(token.getValue());
    }
}
