package shopping.common.auth

import shopping.common.error.ErrorCode

class InvalidTokenException(
    cause: Throwable? = null,
) : LoginFailedException(
        errorCode = ErrorCode.INVALID_TOKEN,
        message = "토큰이 유효하지 않습니다.",
        cause = cause,
    )
