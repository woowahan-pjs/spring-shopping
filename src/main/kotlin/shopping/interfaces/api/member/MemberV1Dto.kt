package shopping.interfaces.api.member

import shopping.domain.member.Member

class MemberV1Dto {

    data class RegisterResponse(
        val id: Long
    ) {
        companion object {
            fun from(member: Member): RegisterResponse {
                return RegisterResponse(
                    id = member.id
                )
            }
        }
    }

    class RegisterRequest(
        val email: String,
        val password: String
    )


}