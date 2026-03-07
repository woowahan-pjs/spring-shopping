package shopping.member.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shopping.member.application.dto.MemberLoginRequest;
import shopping.member.application.dto.MemberLoginResponse;
import shopping.member.application.dto.MemberSignUpRequest;
import shopping.member.application.dto.MemberSignUpResponse;
import shopping.member.application.command.MemberSignUpUseCase;
import shopping.member.application.command.MemberLoginUseCase;
import shopping.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberLoginUseCase memberLoginUseCase;
    private final MemberSignUpUseCase memberSignUpUseCase;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberLoginResponse>> login(@Valid @RequestBody MemberLoginRequest request) {
        MemberLoginResponse response = memberLoginUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<MemberSignUpResponse>> register(@Valid @RequestBody MemberSignUpRequest request) {
        MemberSignUpResponse response = memberSignUpUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(HttpStatus.CREATED, response));
    }
}
