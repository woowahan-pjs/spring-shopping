package shopping.api.presentation.v1.validator

import org.springframework.stereotype.Component
import shopping.api.presentation.v1.dto.LoginMemberRequest
import shopping.api.presentation.v1.dto.RegisterMemberRequest
import shopping.support.error.CoreException
import shopping.support.error.ErrorType

@Component
class MemberValidator {
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    fun validateRegister(request: RegisterMemberRequest) {
        validateEmail(request.email)
        validatePassword(request.password)
    }

    fun validateLogin(request: LoginMemberRequest) {
        validateEmail(request.email)
        validatePassword(request.password)
    }

    private fun validateEmail(email: String) {
        if (email.isBlank()) {
            throw CoreException(ErrorType.INVALID_REQUEST, "이메일을 입력해 주세요.")
        }
        if (!email.matches(emailRegex)) {
            throw CoreException(ErrorType.INVALID_REQUEST, "올바른 이메일 형식이 아니에요.")
        }
    }

    private fun validatePassword(password: String) {
        if (password.isBlank()) {
            throw CoreException(ErrorType.INVALID_REQUEST, "비밀번호를 입력해 주세요.")
        }
    }
}
