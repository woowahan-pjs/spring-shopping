package shopping.auth.application.command

data class LoginCommand(
    val email: String,
    val loginPassword: String,
)
