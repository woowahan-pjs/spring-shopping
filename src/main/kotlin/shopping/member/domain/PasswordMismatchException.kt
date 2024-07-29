package shopping.member.domain

import shopping.common.error.ApiException
import shopping.common.error.ErrorCode

class PasswordMismatchException :
    ApiException(
        errorCode = ErrorCode.PASSWORD_MISMATCH,
        message = "비밀번호가 일치하지 않습니다.",
    )
