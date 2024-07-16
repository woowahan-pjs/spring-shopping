package shopping.user.domain

import shopping.common.error.ApiException
import shopping.common.error.ErrorCode

class UserAlreadyRegisteredException(
    email: String,
) : ApiException(
        errorCode = ErrorCode.USER_ALREADY_REGISTERED,
        message = "email: $email 이미 등록된 사용자입니다.",
    )
