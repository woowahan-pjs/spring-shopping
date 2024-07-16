package shopping.user.application

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import shopping.user.domain.MAX_PASSWORD_LENGTH
import shopping.user.domain.MIN_PASSWORD_LENGTH

data class UserLoginRequest(
    @field:Email
    val email: String,
    @field:Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
    val password: String,
)

data class UserLoginResponse(
    val accessToken: String,
)
