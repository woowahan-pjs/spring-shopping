package shopping.auth.adapter.in.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.auth.adapter.in.web.RefreshTokenCookieManager;
import shopping.auth.service.AuthService;
import shopping.auth.service.AuthTokens;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenCookieManager refreshTokenCookieManager;

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = refreshTokenCookieManager.resolve(request);
        AuthTokens tokens = authService.refresh(refreshToken);
        refreshTokenCookieManager.write(response, tokens.refreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse(tokens.accessToken()));
    }
}
