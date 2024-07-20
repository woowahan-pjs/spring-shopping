package shopping.token.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shopping.token.application.TokenService;
import shopping.token.application.LoginRequest;
import shopping.token.domain.Token;

@RestController
public class TokenController {
    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        Token token = tokenService.generate(loginRequest);
        return ResponseEntity.ok(TokenResponse.from(token));
    }
}
