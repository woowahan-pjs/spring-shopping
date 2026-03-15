package shopping.application.dto

data class RegisterMemberCommand(
    val email: String,
    val rawPassword: String,
)

data class LoginMemberCommand(
    val email: String,
    val rawPassword: String,
)

data class AuthTokenResult(
    val accessToken: String,
)
