package shopping.member.api.command;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shopping.member.api.dto.MemberLoginRequest;
import shopping.member.api.dto.MemberRegisterRequest;
import shopping.member.api.dto.TokenResponse;
import shopping.member.service.MemberCommandService;
import shopping.member.service.dto.MemberLoginInput;
import shopping.member.service.dto.MemberRegisterInput;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberCommandController {

    private final MemberCommandService memberCommandService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(@RequestBody MemberRegisterRequest request) {
        return new TokenResponse(memberCommandService.register(new MemberRegisterInput(request.email(), request.password())).token());
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody MemberLoginRequest request) {
        return new TokenResponse(memberCommandService.login(new MemberLoginInput(request.email(), request.password())).token());
    }
}
