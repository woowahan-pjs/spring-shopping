package shopping.common.auth

import shopping.common.error.ErrorCode

class TokenExpiredException :
    LoginFailedException(
        errorCode = ErrorCode.TOKEN_EXPIRED,
        message = "토큰이 만료되었습니다.",
    )
