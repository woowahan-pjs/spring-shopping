package shopping.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shopping.auth.JwtTokenProvider;
import shopping.auth.controller.dto.RefreshRequest;
import shopping.auth.controller.dto.RefreshResponse;
import shopping.member.domain.Member;
import shopping.member.service.MemberService;

@RequestMapping("/auth")
@RestController
public class AuthController {

    private final JwtTokenProvider provider;
    private final MemberService service;

    public AuthController(JwtTokenProvider provider, MemberService service) {
        this.provider = provider;
        this.service = service;
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody RefreshRequest request) {
        Long memberId = provider.extractMemberIdFromRefreshToken(request.refreshToken());
        Member member = service.findMember(memberId);
        String accessToken = provider.generateAccessToken(member);
        return ResponseEntity.ok(new RefreshResponse(accessToken));
    }
}
