package shopping.common.auth

import shopping.common.error.ApiException
import shopping.common.error.ErrorCode

abstract class LoginFailedException(
    errorCode: ErrorCode,
    message: String,
    cause: Throwable? = null,
) : ApiException(errorCode, message, cause)
