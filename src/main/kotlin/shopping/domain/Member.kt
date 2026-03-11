package shopping.domain

import shopping.support.error.CoreException
import shopping.support.error.ErrorType

class Member(
    val id: Long = 0L,
    val email: String,
    val password: String,
) {
    init {
        validateEmail()
        validatePassword()
    }

    private fun validateEmail() {
        if (email.isBlank()) {
            throw CoreException(ErrorType.INVALID_REQUEST, "이메일은 비어있을 수 없습니다.")
        }
    }

    private fun validatePassword() {
        if (password.isBlank()) {
            throw CoreException(ErrorType.INVALID_REQUEST, "비밀번호는 비어있을 수 없습니다.")
        }
    }

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
