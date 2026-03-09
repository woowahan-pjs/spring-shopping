package shopping.member.adapter.in.api;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.auth.adapter.in.api.TokenResponse;
import shopping.auth.adapter.in.web.RefreshTokenCookieManager;
import shopping.auth.service.AuthTokens;
import shopping.member.service.MemberService;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final RefreshTokenCookieManager refreshTokenCookieManager;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        AuthTokens tokens = memberService.register(request);
        return buildTokenResponse(response, tokens, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthTokens tokens = memberService.login(request);
        return buildTokenResponse(response, tokens, HttpStatus.OK);
    }

    private ResponseEntity<TokenResponse> buildTokenResponse(
            HttpServletResponse response,
            AuthTokens tokens,
            HttpStatus status
    ) {
        refreshTokenCookieManager.write(response, tokens.refreshToken());
        TokenResponse tokenResponse = TokenResponse.from(tokens);
        if (status == HttpStatus.OK) {
            return ResponseEntity.ok(tokenResponse);
        }
        return ResponseEntity.status(status).body(tokenResponse);
    }
}
