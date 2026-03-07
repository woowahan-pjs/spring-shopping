package shopping.member;

import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shopping.member.dto.MemberRequest;
import shopping.member.dto.MemberResponse;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final RegisterMember registerMember;
    private final LoginMember loginMember;

    public MemberController(RegisterMember registerMember, LoginMember loginMember) {
        this.registerMember = registerMember;
        this.loginMember = loginMember;
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody MemberRequest request) {
        String token = registerMember.execute(request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@Valid @RequestBody MemberRequest request) {
        String token = loginMember.execute(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new MemberResponse(token));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
}
