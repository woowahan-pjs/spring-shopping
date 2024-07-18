package shopping.user.application

import shopping.common.error.ApiException
import shopping.common.error.ErrorCode

class UserNotFoundException private constructor(
    message: String,
) : ApiException(
        errorCode = ErrorCode.USER_NOT_FOUND,
        message = message,
    ) {
    companion object {
        fun fromEmail(email: String): UserNotFoundException = UserNotFoundException("email: $email 사용자를 찾을 수 없습니다.")

        fun fromId(id: Long): UserNotFoundException = UserNotFoundException("id: $id 사용자를 찾을 수 없습니다.")
    }
}
