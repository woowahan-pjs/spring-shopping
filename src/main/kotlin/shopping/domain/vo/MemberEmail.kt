package shopping.domain.vo

import shopping.support.error.CoreException
import shopping.support.error.ErrorType

data class MemberEmail(
    val value: String,
) {
    companion object {
        private val FORMAT_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    }

    init {
        validateBlank(value)
        validateFormat(value)
    }

    private fun validateBlank(email: String) {
        if (email.isBlank()) {
            throw CoreException(ErrorType.INVALID_REQUEST, "이메일은 비어있을 수 없습니다.")
        }
    }

    private fun validateFormat(email: String) {
        if (!email.matches(FORMAT_REGEX)) {
            throw CoreException(ErrorType.INVALID_REQUEST, "올바른 이메일 형식이 아니에요.")
        }
    }
}
