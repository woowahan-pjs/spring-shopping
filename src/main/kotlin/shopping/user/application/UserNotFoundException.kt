package shopping.user.application

import shopping.common.error.ApiException
import shopping.common.error.ErrorCode

class UserNotFoundException(
    email: String,
) : ApiException(
        errorCode = ErrorCode.USER_NOT_FOUND,
        message = "email: $email 사용자를 찾을 수 없습니다.",
    )
