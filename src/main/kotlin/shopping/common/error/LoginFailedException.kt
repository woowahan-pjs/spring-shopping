package shopping.common.error

class LoginFailedException(
    message: String,
    cause: Throwable? = null,
) : ApiException(
        errorCode = ErrorCode.LOGIN_FAILED,
        message = message,
        cause = cause,
    )
