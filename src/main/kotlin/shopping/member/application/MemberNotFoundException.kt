package shopping.member.application

import shopping.common.error.ApiException
import shopping.common.error.ErrorCode

class MemberNotFoundException private constructor(
    message: String,
) : ApiException(
        errorCode = ErrorCode.USER_NOT_FOUND,
        message = message,
    ) {
    companion object {
        fun fromEmail(email: String): MemberNotFoundException = MemberNotFoundException("email: $email 사용자를 찾을 수 없습니다.")

        fun fromId(id: Long): MemberNotFoundException = MemberNotFoundException("id: $id 사용자를 찾을 수 없습니다.")
    }
}
