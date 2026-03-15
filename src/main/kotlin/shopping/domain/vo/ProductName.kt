package shopping.domain.vo

import shopping.support.error.CoreException
import shopping.support.error.ErrorType

data class ProductName(
    val value: String,
) {
    companion object {
        // NOTE: 한글, 영문, 숫자, 공백 및 허용된 특수문자만 필터링하는 정규식
        private val FORMAT_REGEX = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]+$".toRegex()
    }

    init {
        validateLength(value)
        validateFormat(value)
    }

    private fun validateLength(name: String) {
        if (name.length > 15) {
            throw CoreException(ErrorType.PRODUCT_NAME_LENGTH_EXCEEDED)
        }
    }

    private fun validateFormat(name: String) {
        if (!name.matches(FORMAT_REGEX)) {
            throw CoreException(ErrorType.PRODUCT_NAME_INVALID_CHARACTERS)
        }
    }
}
