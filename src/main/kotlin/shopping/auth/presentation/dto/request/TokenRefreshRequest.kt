package shopping.auth.presentation.dto.request

import jakarta.validation.constraints.NotBlank
import shopping.auth.application.command.TokenRefreshCommand

data class TokenRefreshRequest(
    @field:NotBlank(message = "토큰 값은 필수 입니다.")
    val refreshToken: String?,
) {
    fun toCommand(): TokenRefreshCommand = TokenRefreshCommand(refreshToken!!)
}
