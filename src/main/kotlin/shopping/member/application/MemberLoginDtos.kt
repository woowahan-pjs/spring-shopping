package shopping.member.application

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import shopping.member.domain.MAX_PASSWORD_LENGTH
import shopping.member.domain.MIN_PASSWORD_LENGTH

data class MemberLoginRequest(
    @field:Email
    val email: String,
    @field:Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
    val password: String,
)

data class MemberLoginResponse(
    val accessToken: String,
)
