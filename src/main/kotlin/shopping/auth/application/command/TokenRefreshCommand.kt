package shopping.auth.application.command

data class TokenRefreshCommand(
    val refreshToken: String,
)
