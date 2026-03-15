package shopping.domain

import shopping.domain.vo.MemberEmail
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

class Member(
    val id: Long = 0L,
    email: String,
    val password: String,
) {
    val email: MemberEmail = MemberEmail(email)

    fun authenticate(
        rawPassword: String,
        encoder: PasswordEncoder,
    ) {
        val isMatched = encoder.matches(rawPassword, this.password)
        if (!isMatched) {
            throw CoreException(ErrorType.MEMBER_NOT_FOUND_OR_INVALID_PASSWORD)
        }
    }

    companion object {
        fun create(
            email: String,
            rawPassword: String,
            encoder: PasswordEncoder,
        ): Member {
            val encodedPassword = encoder.encode(rawPassword)
            return Member(email = email, password = encodedPassword)
        }
    }
}
