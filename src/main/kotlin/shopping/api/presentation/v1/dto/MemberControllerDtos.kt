package shopping.api.presentation.v1.dto

import shopping.application.dto.LoginMemberCommand
import shopping.application.dto.RegisterMemberCommand

data class RegisterMemberRequest(
    val email: String,
    val password: String,
) {
    fun toCommand(): RegisterMemberCommand = RegisterMemberCommand(email = email, rawPassword = password)
}

data class LoginMemberRequest(
    val email: String,
    val password: String,
) {
    fun toCommand(): LoginMemberCommand = LoginMemberCommand(email = email, rawPassword = password)
}

data class AuthTokenResponse(
    val accessToken: String,
)
