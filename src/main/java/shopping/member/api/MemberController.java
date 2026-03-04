package shopping.member.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shopping.member.api.dto.MemberRequest;
import shopping.member.api.dto.TokenResponse;
import shopping.member.service.MemberService;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(@RequestBody MemberRequest request) {
        return memberService.register(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody MemberRequest request) {
        return memberService.login(request);
    }
}
