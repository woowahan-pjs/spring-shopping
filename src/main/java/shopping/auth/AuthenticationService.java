package shopping.auth;

import java.util.UUID;

import org.springframework.stereotype.Component;

import shopping.member.domain.TokenProvider;

@Component
public class AuthenticationService {

    private final TokenProvider tokenProvider;

    public AuthenticationService(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public UUID extractMemberId(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return tokenProvider.extractMemberId(token);
    }
}
