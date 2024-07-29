package shopping.common.auth

import shopping.common.error.ErrorCode

class TokenMissingException :
    LoginFailedException(
        errorCode = ErrorCode.TOKEN_NOT_FOUND,
        message = "인증정보가 없습니다.",
    )
