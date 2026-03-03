package shopping.auth.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import shopping.auth.dto.LoginRequest;
import shopping.auth.dto.LoginResponse;
import shopping.auth.dto.RegisterRequest;
import shopping.auth.dto.RegisterResponse;
import shopping.auth.service.AuthFacade;

@Tag(name = "[회원] 회원 관리 API", description = "전반적인 회원 생명주기에 대한 관리를 담당합니다.")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/register")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
                @ApiResponse(responseCode = "400", description = "입력 값이 잘못 되었을 경우"),
                @ApiResponse(responseCode = "409", description = "이미 가입 된 이메일"),
                @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    @Operation(summary = "회원 가입", description = "회원 가입을 합니다.")
    public RegisterResponse register(@Valid @RequestBody final RegisterRequest request) {
        return authFacade.register(request);
    }

    @PostMapping("/login")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "로그인 성공"),
                @ApiResponse(
                        responseCode = "400",
                        description = "입력 값이 잘못 되거나, 유효하지 않은 정보, 비밀번호가 불일치한 경우"),
                @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    @Operation(summary = "로그인", description = "로그인을 합니다.")
    public LoginResponse login(@Valid @RequestBody final LoginRequest request) {
        return authFacade.login(request);
    }
}
