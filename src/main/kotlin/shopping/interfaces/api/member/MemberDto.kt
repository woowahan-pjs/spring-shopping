package shopping.interfaces.api.member

import shopping.domain.member.Member

class MemberDto {
    data class RegisterResponse(
        val id: Long,
    ) {
        companion object {
            fun from(member: Member): RegisterResponse =
                RegisterResponse(
                    id = member.id,
                )
        }
    }

    data class RegisterRequest(
        val email: String,
        val password: String,
    )

    data class LoginRequest(
        val email: String,
        val password: String,
    )

    data class LoginResponse(
        val token: String,
    ) {
        companion object {
            fun from(token: String): LoginResponse =
                LoginResponse(
                    token = token,
                )
        }
    }
}
