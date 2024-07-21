package shopping.auth.presentation

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import shopping.auth.application.AuthenticationCommandService
import shopping.auth.presentation.dto.request.LoginRequest
import shopping.auth.presentation.dto.request.TokenRefreshRequest
import shopping.auth.presentation.dto.response.LoginResponse
import shopping.auth.presentation.dto.response.TokenRefreshResponse
import shopping.global.common.SuccessResponse

@RestController
class AuthenticationApi(private val authenticationCommandService: AuthenticationCommandService) {
    @PostMapping("/api/auth/login")
    fun logIn(
        @RequestBody @Valid request: LoginRequest,
    ): SuccessResponse<LoginResponse> = SuccessResponse(LoginResponse(authenticationCommandService.logIn(request.toCommand())))

    @PostMapping("/api/auth/refresh")
    fun refresh(
        @RequestBody @Valid request: TokenRefreshRequest,
    ): SuccessResponse<TokenRefreshResponse> =
        SuccessResponse(TokenRefreshResponse(authenticationCommandService.refreshToken(request.toCommand())))
}
