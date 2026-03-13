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
        Member member = service.login(request.getEmail(), request.getPassword());
        String accessToken = provider.generate(member.getId());
        return ResponseEntity.ok(new LoginResponse(accessToken));
    }
}
