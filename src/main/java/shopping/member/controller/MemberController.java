package shopping.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shopping.auth.JwtTokenProvider;
import shopping.member.domain.Member;
import shopping.member.controller.dto.LoginResponse;
import shopping.member.controller.dto.MemberRequest;
import shopping.member.service.MemberService;

@RestController
public class MemberController {
    private final MemberService service;
    private final JwtTokenProvider provider;

    public MemberController(MemberService service, JwtTokenProvider provider) {
        this.service = service;
        this.provider = provider;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> register(@RequestBody MemberRequest request) {
        service.register(request.toMember());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody MemberRequest request) {
        Member member = service.login(request.email(), request.password());
        String accessToken = provider.generateAccessToken(member);
        String refreshToken = provider.generateRefreshToken(member);
        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    /**
     * 관리자 계정 생성 엔드포인트.
     * 회원 도메인의 단일 진입점 유지를 위해 MemberController에서 처리하며,
     * 관리자 기능이 확장될 경우 AdminController로 분리한다.
    **/
    @PostMapping("/admin/members")
    public ResponseEntity<Void> adminRegister(@RequestBody MemberRequest request) {
        service.adminRegister(request.toMember());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
